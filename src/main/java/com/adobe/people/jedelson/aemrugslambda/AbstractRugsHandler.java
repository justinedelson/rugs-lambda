package com.adobe.people.jedelson.aemrugslambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atomist.project.archive.Rugs;

/**
 * Created by jedelson on 3/28/17.
 */
public abstract class AbstractRugsHandler<I, O> implements RequestHandler<I, O> {

    @Override
    public O handleRequest(I input, Context context) {
        RugWrapper rugWrapper = null;
        try {
            rugWrapper = new RugWrapper(context.getAwsRequestId(), "aem-rugs", "aem-rugs", "0.2.2");
            Rugs rugs = rugWrapper.getRugs();
            return handleRequest(input, context, rugs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            rugWrapper.cleanup();
        }
    }

    protected abstract O handleRequest(I input, Context context, Rugs rugs);
}
