package com.adobe.people.jedelson.rugsinlambda;

public class NoSuchGeneratorException extends RuntimeException {

    private final String generatorName;

    public NoSuchGeneratorException(String generatorName) {
        this.generatorName = generatorName;
    }

    @Override
    public String getMessage() {
        return "Generator '" + generatorName + "' was not found.";
    }
}
