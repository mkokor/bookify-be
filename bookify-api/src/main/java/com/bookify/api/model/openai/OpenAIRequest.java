package com.bookify.api.model.openai;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAIRequest {

    private String title;   // Polje za naziv knjige
    private String author;  // Polje za autora knjige
    private List<String> genres;   // Novi parametar za žanrove
    private List<String> authors;

    // Getters and Setters

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


}