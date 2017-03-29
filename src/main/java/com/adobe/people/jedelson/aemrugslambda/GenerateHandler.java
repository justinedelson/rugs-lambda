package com.adobe.people.jedelson.aemrugslambda;

import com.adobe.people.jedelson.aemrugslambda.dto.GenerationRequestDTO;
import com.adobe.people.jedelson.aemrugslambda.dto.GenerationResultDTO;
import com.adobe.people.jedelson.aemrugslambda.dto.ValidationResultDTO;
import com.adobe.people.jedelson.aemrugslambda.helpers.TempProjectManagement;
import com.amazonaws.services.lambda.runtime.Context;
import com.atomist.param.ParameterValues;
import com.atomist.project.archive.Rugs;
import com.atomist.project.generate.ProjectGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Optional;

import static scala.collection.JavaConversions.asJavaCollection;


public class GenerateHandler extends AbstractRugsHandler<GenerationRequestDTO, GenerationResultDTO> {

    private static final Logger log = LoggerFactory.getLogger(GenerateHandler.class);

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
                TempProjectManagement tpm = new TempProjectManagement();
                tpm.generate(generator, paramValues, projectName);

                File zipFile = tpm.createZipFile();
                log.info("zip file is at {} length is {}.", zipFile.getAbsolutePath(), zipFile.length());

                return new GenerationResultDTO(true);
            }
        } else {
            throw new NoSuchGeneratorException(input.getGeneratorName());
        }
    }
}
