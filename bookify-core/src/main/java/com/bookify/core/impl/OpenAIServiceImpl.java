package com.bookify.core.impl;

import com.bookify.api.OpenAIService;
import com.bookify.api.model.openai.OpenAIRequest;
import com.bookify.api.model.openai.OpenAIResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Service
public class OpenAIServiceImpl implements OpenAIService{

    @Value("${huggingface.api.key}")
    private String huggingFaceApiKey;

    private static final String API_URL = "https://api-inference.huggingface.co/models/gpt2";

    @Override
    public OpenAIResponse generateText(OpenAIRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + huggingFaceApiKey);
        headers.set("Content-Type", "application/json");

        String requestBody = createRequestBody(request);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<Map<String, String>>> response = restTemplate.exchange(
                API_URL,
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<List<Map<String, String>>>() {}  // Promenjeno na OpenAIResponse[], jer je odgovor niz objekata
        );

        List<Map<String, String>> responseBody = response.getBody();
        if (responseBody != null) {
            List<String> generatedTexts = responseBody.stream()
                    .map(map -> map.get("generated_text")) // Ekstrahuj samo `generated_text`
                    .toList();

            OpenAIResponse openAIResponse = new OpenAIResponse();
            openAIResponse.setGenerated_text(generatedTexts);
            return openAIResponse;
        }
        return null;
    }

    private String createRequestBody(OpenAIRequest request) {
        // Kreiraj prompt koji uključuje naslov i autora knjige
        String prompt = String.format(
                "Provide a concise summary of the average reader's rating for '%s' by %s. Include a hypothetical star rating (e.g., 'Readers give this book a 4.5-star review for its themes and storytelling').",
                request.getTitle(),
                request.getAuthor()
        );

        // Vratiti JSON format za zahtjev prema Hugging Face API-u
        return String.format(
                "{ \"inputs\": \"%s\", \"parameters\": { \"max_length\": %d, \"temperature\": %.2f, \"top_p\": %.2f, \"repetition_penalty\": %.2f, \"num_return_sequences\": %d } }",
                prompt,
                request.getMaxTokens(),
                request.getTemperature(),
                request.getTopP(),
                request.getRepetitionPenalty(),
                request.getNumReturnSequences()
        );
    }
}
