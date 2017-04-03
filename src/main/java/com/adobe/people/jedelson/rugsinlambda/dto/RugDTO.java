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
package com.adobe.people.jedelson.rugsinlambda.dto;

import com.atomist.rug.runtime.ParameterizedRug;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.stream.Collectors;

public class RugDTO {

    private final String name;
    private final String description;
    private final List<ParameterDTO> parameters;

    public RugDTO(ParameterizedRug rug) {
        this.name = rug.name();
        this.description = rug.description();
        parameters = JavaConversions.asJavaCollection(rug.parameters()).stream().map(p -> {
            return new ParameterDTO(p);
        }).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<ParameterDTO> getParameters() {
        return parameters;
    }
}
