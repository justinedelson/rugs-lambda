package com.adobe.people.jedelson.aemrugslambda.dto;

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
