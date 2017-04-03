package com.adobe.people.jedelson.rugsinlambda.dto;

public class GenerationResultDTO extends ValidationResultDTO {

    private String url;

    public GenerationResultDTO(boolean result) {
        super(result);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
