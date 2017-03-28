package com.adobe.people.jedelson.aemrugslambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

/**
 * Created by jedelson on 3/28/17.
 */
public class ParametersHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>  {
    private static final Logger LOG = Logger.getLogger(ParametersHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("received: " + input);
        Response responseBody = new Response("GOOOOO Serverless v1.x! Your function executed successfully!", input);
        return ApiGatewayResponse.builder()
                .setStatusCode(200)
                .setObjectBody(responseBody)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }

}
