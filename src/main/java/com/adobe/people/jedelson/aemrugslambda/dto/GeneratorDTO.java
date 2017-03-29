package com.adobe.people.jedelson.aemrugslambda.dto;

import com.atomist.project.generate.ProjectGenerator;
import scala.collection.JavaConversions;

import java.util.List;
import java.util.stream.Collectors;

public class GeneratorDTO {

    private final String name;
    private final String description;
    private final List<ParameterDTO> parameters;

    public GeneratorDTO(ProjectGenerator generator) {
        this.name = generator.name();
        this.description = generator.description();
        parameters = JavaConversions.asJavaCollection(generator.parameters()).stream().map(p -> {
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
