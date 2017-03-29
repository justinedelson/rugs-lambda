package com.adobe.people.jedelson.aemrugslambda.dto;

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
