package com.adobe.people.jedelson.rugsinlambda;

import com.adobe.people.jedelson.rugsinlambda.helpers.RugWrapper;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.atomist.project.archive.Rugs;

public abstract class AbstractRugsHandler<I, O> implements RequestHandler<I, O> {

    @Override
    public O handleRequest(I input, Context context) {
        RugWrapper rugWrapper = null;
        try {
            rugWrapper = new RugWrapper(context.getAwsRequestId());
            Rugs rugs = rugWrapper.getRugs();
            return handleRequest(input, context, rugs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (rugWrapper != null) {
                rugWrapper.cleanup();
            }
        }
    }

    protected abstract O handleRequest(I input, Context context, Rugs rugs);
}
