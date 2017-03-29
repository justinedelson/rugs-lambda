package com.adobe.people.jedelson.aemrugslambda.dto;

import com.atomist.param.ParameterValue;
import com.atomist.param.ParameterValues;
import com.atomist.param.SimpleParameterValue;
import com.atomist.param.SimpleParameterValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static scala.collection.JavaConversions.asScalaBuffer;

public class GenerationRequestDTO {

    private String generatorName;

    private Map<String, String> params = new HashMap<>();

    public String getGeneratorName() {
        return generatorName;
    }

    public void setGeneratorName(String generatorName) {
        this.generatorName = generatorName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public ParameterValues toParameterValues() {
        return new SimpleParameterValues(asScalaBuffer(
                params.entrySet().stream().map(e -> {
                    return new SimpleParameterValue(e.getKey(), e.getValue());
                }).collect(Collectors.toList())
        ));
    }

    @Override
    public String toString() {
        return "GenerationRequestDTO{" +
                "generatorName='" + generatorName + '\'' +
                ", params=" + params +
                '}';
    }
}
