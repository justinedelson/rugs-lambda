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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static scala.collection.JavaConversions.asScalaBuffer;

public class EditRequestDTO {

    private String name;

    private Map<String, String> params = new HashMap<>();

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public ParameterValues toParameterValues(Map<String, String> baseParams) {
        Map<String, String> allParams = new HashMap<>();
        allParams.putAll(baseParams);
        allParams.putAll(params);

        return new SimpleParameterValues(asScalaBuffer(
                allParams.entrySet().stream().map(e -> {
                    return new SimpleParameterValue(e.getKey(), e.getValue());
                }).collect(Collectors.toList())
        ));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
