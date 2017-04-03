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

import com.atomist.param.Parameter;

public class ParameterDTO {

    private final String displayName;
    private final int minLength;
    private final int maxLength;
    private final String name;
    private final String description;
    private final String validInputDescription;
    private final String pattern;
    private final boolean required;

    public ParameterDTO(Parameter param) {
        this.name = param.name();
        this.description = param.description();
        this.displayName = param.getDisplayName();
        this.minLength = param.getMinLength();
        this.maxLength = param.getMaxLength();
        this.validInputDescription = param.getValidInputDescription();
        this.pattern = param.getPattern();
        this.required = param.isRequired();
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public String getValidInputDescription() {
        return validInputDescription;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isRequired() {
        return required;
    }
}
