package com.bookify.rest.openai;

import com.bookify.api.OpenAIService;
import com.bookify.api.model.openai.OpenAIRequest;
import com.bookify.api.model.openai.OpenAIResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/openai")
public class OpenAIController {

    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/generate-text")
    public OpenAIResponse generateText(@RequestBody OpenAIRequest request) {
        return openAIService.generateText(request);
    }
}