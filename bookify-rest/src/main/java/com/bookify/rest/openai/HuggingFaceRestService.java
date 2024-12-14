package com.bookify.rest.openai;

import com.bookify.api.HuggingFaceService;
import com.bookify.api.model.openai.HuggingFaceRequest;
import com.bookify.api.model.openai.HuggingFaceResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/huggingface")
public class HuggingFaceRestService {

    private final HuggingFaceService openAIService;

    public HuggingFaceRestService(HuggingFaceService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/generate-text")
    public HuggingFaceResponse generateText(@RequestBody HuggingFaceRequest request) {
        return openAIService.generateText(request);
    }
}