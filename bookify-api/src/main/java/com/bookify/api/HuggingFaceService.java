package com.bookify.api;
import com.bookify.api.model.openai.HuggingFaceRequest;
import com.bookify.api.model.openai.HuggingFaceResponse;

public interface HuggingFaceService {

    HuggingFaceResponse generateText(HuggingFaceRequest request);
}
