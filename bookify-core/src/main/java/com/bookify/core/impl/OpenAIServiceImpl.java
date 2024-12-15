package com.bookify.core.impl;

import com.bookify.api.OpenAIService;
import com.bookify.api.model.openai.OpenAIRequest;
import com.bookify.api.model.openai.OpenAIResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OpenAIServiceImpl implements OpenAIService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String generateSummary(OpenAIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");
        String requestBody = createSummaryRequestBody(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                openAiApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            // Parse the response and return the content
            OpenAIResponse openAIResponse = objectMapper.readValue(response.getBody(), OpenAIResponse.class);

            if (openAIResponse.getChoices() != null && !openAIResponse.getChoices().isEmpty()) {
                return openAIResponse.getChoices().get(0).getMessage().getContent();
            } else {
                return "No content returned from OpenAI API.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing OpenAI response.";
        }
    }

    @Override
    public String generateRating(OpenAIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");
        String requestBody = createRatingRequestBody(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                openAiApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            // Parse the response and return the content
            OpenAIResponse openAIResponse = objectMapper.readValue(response.getBody(), OpenAIResponse.class);

            if (openAIResponse.getChoices() != null && !openAIResponse.getChoices().isEmpty()) {
                return openAIResponse.getChoices().get(0).getMessage().getContent();
            } else {
                return "No content returned from OpenAI API.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing OpenAI response.";
        }
    }

    @Override
    public String suggestBooks(OpenAIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiApiKey);
        headers.set("Content-Type", "application/json");

        Object requestBody = createSuggestionRequestBody(request);
        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(requestBody);
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating request body.";
        }
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                openAiApiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        try {
            OpenAIResponse openAIResponse = objectMapper.readValue(response.getBody(), OpenAIResponse.class);

            if (openAIResponse.getChoices() != null && !openAIResponse.getChoices().isEmpty()) {
                return openAIResponse.getChoices().get(0).getMessage().getContent();
            } else {
                return "No suggestions returned from OpenAI API.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error parsing OpenAI response.";
        }
    }

    private Object createSuggestionRequestBody(OpenAIRequest request) {
        // Kreiranje prompta na osnovu žanrova i autora
        StringBuilder prompt = new StringBuilder("Suggest some books that match the following preferences:\n");
        if (request.getGenres() != null && !request.getGenres().isEmpty()) {
            List<String> filteredGenres = request.getGenres()
                    .stream()
                    .filter(genre -> genre != null && !genre.isEmpty())
                    .toList();
            if (!filteredGenres.isEmpty()) {
                prompt.append("Genres: ").append(String.join(", ", filteredGenres)).append(". ");
            }
        }

        if (request.getAuthors() != null && !request.getAuthors().isEmpty()) {
            List<String> filteredAuthors = request.getAuthors()
                    .stream()
                    .filter(author -> author != null && !author.isEmpty())
                    .toList();
            if (!filteredAuthors.isEmpty()) {
                prompt.append("Authors: ").append(String.join(", ", filteredAuthors)).append(". ");
            }
        }

        // Kreiraj JSON objekt
        return new Object() {
            public final String model = "gpt-3.5-turbo";
            public final List<Object> messages = List.of(new Object() {
                public final String role = "user";
                public final String content = prompt.toString();
            });
            public final double temperature = 0.7;
            public final int max_tokens = 150;
        };
    }

    private String createSummaryRequestBody(OpenAIRequest request) {
        return "{\n" +
                "  \"model\": \"gpt-3.5-turbo\",\n" +
                "  \"messages\": [\n" +
                "    {\"role\": \"user\", \"content\": \"Please summarize the book \\\""
                + request.getTitle() + "\\\" by \\\"" + request.getAuthor()+ "\\\".\"}\n" +
                "  ],\n" +
                "  \"temperature\": 0.7,\n" +
                "  \"max_tokens\": 100\n" +
                "}";
    }


    private String createRatingRequestBody(OpenAIRequest request) {
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