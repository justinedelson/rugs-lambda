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
