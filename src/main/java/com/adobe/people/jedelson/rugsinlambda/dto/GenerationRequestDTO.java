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
