package com.bookify.api.model.openai;

import java.util.List;

public class HuggingFaceResponse {

    private List<String> generated_text; // Promenjeno na generated_text, kako bi odgovaralo formatu

    public List<String> getGenerated_text() {
        return generated_text;
    }

    public void setGenerated_text(List<String> generated_text) {
        this.generated_text = generated_text;
    }

}