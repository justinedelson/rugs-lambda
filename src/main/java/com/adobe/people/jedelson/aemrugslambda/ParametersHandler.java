package com.adobe.people.jedelson.aemrugslambda;

import com.adobe.people.jedelson.aemrugslambda.dto.GenerationRequestDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atomist.project.archive.Rugs;
import com.atomist.project.generate.ProjectGenerator;
import org.apache.log4j.Logger;
import scala.collection.JavaConversions;

import java.util.Collections;
import java.util.Optional;

import static scala.collection.JavaConversions.asJavaCollection;

/**
 * Created by jedelson on 3/28/17.
 */
public class ParametersHandler implements RequestHandler<GenerationRequestDTO, ApiGatewayResponse> {
    private static final Logger LOG = Logger.getLogger(ParametersHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(final GenerationRequestDTO input, Context context) {
        LOG.info("received: " + input.toString());
        RugWrapper rugWrapper = null;
        try {
            rugWrapper = new RugWrapper(context.getAwsRequestId(), "aem-rugs", "aem-rugs", "0.2.2");
            Rugs rugs = rugWrapper.getRugs();
            Optional<ProjectGenerator> opt = asJavaCollection(rugs.generators()).stream()
                    .filter(g -> g.name().equals(input.getGeneratorName())).findFirst();
            if (opt.isPresent()) {
                ProjectGenerator generator = opt.get();
                return ApiGatewayResponse.builder()
                        .setStatusCode(200)
                        .setObjectBody(JavaConversions.asJavaCollection(generator.parameters()))
                        .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                        .build();
            } else {
                throw new NoSuchGeneratorException(input.getGeneratorName());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            rugWrapper.cleanup();
        }

/*

        Response responseBody = new Response("GOOOOO Serverless v1.x! Your function executed successfully! "
                + rugWrapper.getArchive().getAbsolutePath() + "|" + rugWrapper.getArchive().exists() + "|" + rugWrapper.getArchive().length(), input);
        try {
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(responseBody)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        } */
    }

}
