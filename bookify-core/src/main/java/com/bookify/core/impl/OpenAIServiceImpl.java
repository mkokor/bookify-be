package com.bookify.core.impl;

import com.bookify.api.OpenAIService;
import com.bookify.api.model.openai.OpenAIRequest;
import com.bookify.api.model.openai.OpenAIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    //private static final String MODEL_NAME = "gpt-3.5-turbo";

    @Override
    public String generateRating(OpenAIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = createRequestBody(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                openAiApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody(); // Vraća sirovi JSON odgovor kao string
    }

    private String createRequestBody(OpenAIRequest request) {
        return "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"What is the average rating of the book \\\""
                + request.getTitle() + "\\\" by author \\\"" + request.getAuthor() + "\\\"?\"}\n" +
                "  ],\n" +
                "  \"temperature\": 0.7,\n" +
                "  \"max_tokens\": 100\n" +
                "}";
    }
}