package com.bookify.rest.openai;
import com.bookify.api.OpenAIService;
import com.bookify.api.model.openai.OpenAIRequest;
import com.bookify.api.model.openai.OpenAIResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/openai")
public class OpenAIRestService {

    private final OpenAIService openAIService;

    public OpenAIRestService(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping("/generate-rating")
    public String generateBookRating(@RequestBody OpenAIRequest request) {
        return openAIService.generateRating(request);
    }

    @PostMapping("/generate-summary")
    public String generateBookSummary(@RequestBody OpenAIRequest request) {
        return openAIService.generateSummary(request);
    }

    @PostMapping("/suggest-books")
    public String suggestBooks(@RequestBody OpenAIRequest request) {
        return openAIService.suggestBooks(request);
    }
}