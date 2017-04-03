/*
 * #%L
 * %%
 * Copyright (C) 2017 Adobe
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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
