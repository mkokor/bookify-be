package com.bookify.api.model.book;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class BookRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "Title must be specified.")
    private String title;
    @NotBlank(message = "Author must be specified.")
    private String author;
    @NotBlank(message = "Issue date must be specified.")
    private LocalDateTime issueDate;
    private String genre;
    private String description;
    private int numberOfPages;
    private int copiesAvailable;
    private String bookUrl;
}