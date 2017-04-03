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

import com.adobe.people.jedelson.rugsinlambda.dto.RugsDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.atomist.project.archive.Rugs;


import static scala.collection.JavaConversions.asJavaCollection;

public class RugsHandler extends AbstractRugsHandler<Void, RugsDTO> {

    @Override
    protected RugsDTO handleRequest(Void aVoid, Context context, Rugs rugs) {
        RugsDTO result = new RugsDTO();
        asJavaCollection(rugs.generators()).stream().forEach(g -> {
           result.addGenerator(g);
        });
        asJavaCollection(rugs.editors()).stream().forEach(g -> {
            result.addEditor(g);
        });
        return result;
    }
}
