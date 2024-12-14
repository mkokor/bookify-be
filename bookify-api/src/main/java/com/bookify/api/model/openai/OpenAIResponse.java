package com.bookify.api.model.openai;

import lombok.Data;

@Data
public class OpenAIResponse {
    private String rating;
    private String review;
}
