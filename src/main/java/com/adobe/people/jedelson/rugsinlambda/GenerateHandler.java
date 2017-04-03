package com.adobe.people.jedelson.rugsinlambda;

import com.adobe.people.jedelson.rugsinlambda.dto.EditRequestDTO;
import com.adobe.people.jedelson.rugsinlambda.dto.GenerationRequestDTO;
import com.adobe.people.jedelson.rugsinlambda.dto.GenerationResultDTO;
import com.adobe.people.jedelson.rugsinlambda.helpers.TempProjectManagement;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.atomist.param.ParameterValues;
import com.atomist.project.archive.Rugs;
import com.atomist.project.edit.ProjectEditor;
import com.atomist.project.generate.ProjectGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.Optional;

import static scala.collection.JavaConversions.asJavaCollection;


public class GenerateHandler extends AbstractRugsHandler<GenerationRequestDTO, GenerationResultDTO> {

    private static final Logger log = LoggerFactory.getLogger(GenerateHandler.class);
    public static final String BUCKET_NAME = System.getenv("BUCKET_NAME");

    @Override
    protected GenerationResultDTO handleRequest(GenerationRequestDTO input, Context context, Rugs rugs) {
        String generatorName = (String) input.getGeneratorName();
        log.info("Using {} as generator name from {}.", generatorName, input);


        Optional<ProjectGenerator> opt = asJavaCollection(rugs.generators()).stream()
                .filter(g -> g.name().equals(input.getGeneratorName())).findFirst();
        if (opt.isPresent()) {
            ProjectGenerator generator = opt.get();
            ParameterValues paramValues = input.toParameterValues();
            if (!generator.areValid(paramValues)) {
                GenerationResultDTO result =  new GenerationResultDTO(false);
                asJavaCollection(generator.findInvalidParameterValues(paramValues)).forEach(p -> {
                    result.addInvalidParameter(p);
                });
                asJavaCollection(generator.findMissingParameters(paramValues)).forEach(p -> {
                    result.addMissingParameter(p);
                });
                return result;
            } else {
                String projectName = input.getParams().get("project_name");
                TempProjectManagement tpm = new TempProjectManagement(context.getAwsRequestId());
                tpm.generate(generator, paramValues, projectName);

                GenerationResultDTO result = new GenerationResultDTO(true);

                for (EditRequestDTO edit : input.getEditors()) {
                    String editorName = edit.getName();
                    log.info("Editing with {} using params {}.", editorName, edit.getParams());
                    Optional<ProjectEditor> editorOpt = asJavaCollection(rugs.editors()).stream()
                            .filter(g -> g.name().equals(editorName)).findFirst();
                    if (editorOpt.isPresent()) {
                        ProjectEditor editor = editorOpt.get();
                        ParameterValues editorParams = edit.toParameterValues(input.getParams());
                        if (!editor.areValid(editorParams)) {
                            asJavaCollection(generator.findInvalidParameterValues(paramValues)).forEach(p -> {
                                result.addInvalidParameter(editorName, p);
                            });
                            asJavaCollection(generator.findMissingParameters(paramValues)).forEach(p -> {
                                result.addMissingParameter(editorName, p);
                            });
                        } else {
                            tpm.edit(editor, editorParams, projectName);
                        }
                    }
                }


                File zipFile = tpm.createZipFile();
                log.info("zip file is at {} length is {}.", zipFile.getAbsolutePath(), zipFile.length());

                AmazonS3Client s3Client = new AmazonS3Client();
                String keyName = context.getAwsRequestId() + "/project.zip";
                s3Client.putObject(BUCKET_NAME, keyName, zipFile);

                Date expiration = new Date();
                long msec = expiration.getTime();
                msec += 1000 * 60 * 60; // 1 hour.
                expiration.setTime(msec);

                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(BUCKET_NAME, keyName);
                generatePresignedUrlRequest.setMethod(HttpMethod.GET);
                generatePresignedUrlRequest.setExpiration(expiration);

                URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

                result.setUrl(presignedUrl.toString());
                return result;
            }
        } else {
            throw new NoSuchGeneratorException(input.getGeneratorName());
        }
    }
}
