package com.bookify.api.model.openai;


public class HuggingFaceRequest {

    private String model;
    private String prompt;
    private int maxTokens;
    private String title;   // Polje za naziv knjige
    private String author;  // Polje za autora knjige
    private double temperature;       // Novi parametar
    private double topP;              // Novi parametar
    private double repetitionPenalty; // Novi parametar
    private int numReturnSequences;   // Novi parametar

    // Getters and Setters
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        this.maxTokens = maxTokens;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTopP() {
        return topP;
    }

    public void setTopP(double topP) {
        this.topP = topP;
    }

    public double getRepetitionPenalty() {
        return repetitionPenalty;
    }

    public void setRepetitionPenalty(double repetitionPenalty) {
        this.repetitionPenalty = repetitionPenalty;
    }

    public int getNumReturnSequences() {
        return numReturnSequences;
    }

    public void setNumReturnSequences(int numReturnSequences) {
        this.numReturnSequences = numReturnSequences;
    }
}
