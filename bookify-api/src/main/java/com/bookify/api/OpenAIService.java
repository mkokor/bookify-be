package com.bookify.api;
import com.bookify.api.model.openai.OpenAIRequest;
import com.bookify.api.model.openai.OpenAIResponse;

public interface OpenAIService {

    OpenAIResponse generateText(OpenAIRequest request);
}
