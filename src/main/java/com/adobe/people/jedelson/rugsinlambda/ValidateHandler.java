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

import com.adobe.people.jedelson.rugsinlambda.dto.GenerationRequestDTO;
import com.adobe.people.jedelson.rugsinlambda.dto.ValidationResultDTO;
import com.amazonaws.services.lambda.runtime.Context;
import com.atomist.param.ParameterValues;
import com.atomist.project.archive.Rugs;
import com.atomist.project.generate.ProjectGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static scala.collection.JavaConversions.asJavaCollection;

public class ValidateHandler extends AbstractRugsHandler<GenerationRequestDTO, ValidationResultDTO> {

    private static final Logger log = LoggerFactory.getLogger(ValidateHandler.class);

    @Override
    protected ValidationResultDTO handleRequest(GenerationRequestDTO input, Context context, Rugs rugs) {
        String generatorName = (String) input.getGeneratorName();
        log.info("Using {} as generator name from {}.", generatorName, input);

        Optional<ProjectGenerator> opt = asJavaCollection(rugs.generators()).stream()
                .filter(g -> g.name().equals(input.getGeneratorName())).findFirst();
        if (opt.isPresent()) {
            ProjectGenerator generator = opt.get();
            ParameterValues paramValues = input.toParameterValues();
            if (!generator.areValid(paramValues)) {
                ValidationResultDTO result =  new ValidationResultDTO(false);
                asJavaCollection(generator.findInvalidParameterValues(paramValues)).forEach(p -> {
                    result.addInvalidParameter(p);
                });
                asJavaCollection(generator.findMissingParameters(paramValues)).forEach(p -> {
                    result.addMissingParameter(p);
                });
                return result;
            } else {
                return new ValidationResultDTO(true);
            }
        } else {
            throw new NoSuchGeneratorException(input.getGeneratorName());
        }
    }
}
