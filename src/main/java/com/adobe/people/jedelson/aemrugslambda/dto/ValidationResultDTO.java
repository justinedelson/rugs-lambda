package com.adobe.people.jedelson.aemrugslambda.dto;

import com.atomist.param.Parameter;
import com.atomist.param.ParameterValue;

import java.util.ArrayList;
import java.util.List;

public class ValidationResultDTO {

    private final boolean result;

    private final List<String> invalidParameterNames = new ArrayList<>();

    private final List<String> missingParameterNames = new ArrayList<>();

    public ValidationResultDTO(boolean result) {
        this.result = result;
    }

    public boolean getResult() {
        return result;
    }

    public List<String> getInvalidParameterNames() {
        return invalidParameterNames;
    }

    public List<String> getMissingParameterNames() {
        return missingParameterNames;
    }

    public void addInvalidParameter(ParameterValue p) {
        invalidParameterNames.add(p.getName());
    }

    public void addMissingParameter(Parameter p) { missingParameterNames.add(p.getName()); }

    public void addMissingParameter(String name) {
        missingParameterNames.add(name);
    }
}
