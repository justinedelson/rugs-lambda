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

import com.atomist.param.ParameterValues;
import com.atomist.param.SimpleParameterValue;
import com.atomist.param.SimpleParameterValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scala.collection.JavaConversions.asScalaBuffer;

public class GenerationRequestDTO {

    private String generatorName;

    private List<EditRequestDTO> editors = new ArrayList<>();

    private Map<String, String> params = new HashMap<>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getGeneratorName() {
        return generatorName;
    }

    public void setGeneratorName(String generatorName) {
        this.generatorName = generatorName;
    }

    public List<EditRequestDTO> getEditors() {
        if (editors == null) {
            return Collections.emptyList();
        } else {
            return editors;
        }
    }

    public void setEditors(List<EditRequestDTO> editors) {
        this.editors = editors;
    }

    @Override
    public String toString() {
        return "GenerationRequestDTO{" +
                "generatorName='" + generatorName + '\'' +
                ", params=" + params +
                ", editors=" + editors +
                '}';
    }

    public ParameterValues toParameterValues() {
        return new SimpleParameterValues(asScalaBuffer(
                params.entrySet().stream().map(e -> {
                    return new SimpleParameterValue(e.getKey(), e.getValue());
                }).collect(Collectors.toList())
        ));
    }
}
