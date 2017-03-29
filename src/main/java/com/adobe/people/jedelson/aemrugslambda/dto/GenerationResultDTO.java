package com.adobe.people.jedelson.aemrugslambda.dto;

public class GenerationResultDTO extends ValidationResultDTO {

    private final String url;

    public GenerationResultDTO(boolean result) {
        super(result);
        url = null;
    }

    public GenerationResultDTO(String url) {
        super(true);
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
