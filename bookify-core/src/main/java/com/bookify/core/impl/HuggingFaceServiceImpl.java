package com.bookify.core.impl;

import com.bookify.api.HuggingFaceService;
import com.bookify.api.model.openai.HuggingFaceRequest;
import com.bookify.api.model.openai.HuggingFaceResponse;
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
public class HuggingFaceServiceImpl implements HuggingFaceService {

    @Value("${huggingface.api.key}")
    private String huggingFaceApiKey;

    private static final String API_URL = "https://api-inference.huggingface.co/models/gpt2";

    @Override
    public HuggingFaceResponse generateText(HuggingFaceRequest request) {
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

            HuggingFaceResponse openAIResponse = new HuggingFaceResponse();
            openAIResponse.setGenerated_text(generatedTexts);
            return openAIResponse;
        }
        return null;
    }

    private String createRequestBody(HuggingFaceRequest request) {
        // Kreiraj prompt koji uključuje naslov i autora knjige
        String prompt = String.format(
                "Give me a number rating for '%s' by %s.",
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
