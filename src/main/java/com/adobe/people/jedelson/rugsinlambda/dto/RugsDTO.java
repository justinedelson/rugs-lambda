package com.adobe.people.jedelson.rugsinlambda.dto;

import com.atomist.rug.runtime.ParameterizedRug;

import java.util.ArrayList;
import java.util.List;

public class RugsDTO {

    private final List<RugDTO> editors = new ArrayList<>();
    private final List<RugDTO> generators = new ArrayList<>();

    public List<RugDTO> getEditors() {
        return editors;
    }

    public List<RugDTO> getGenerators() {
        return generators;
    }

    public void addGenerator(ParameterizedRug g) {
        generators.add(new RugDTO(g));
    }

    public void addEditor(ParameterizedRug e) {
        editors.add(new RugDTO(e));
    }
}
