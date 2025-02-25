package com.bookify.api.model.book;

import com.bookify.api.model.library.LibraryResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Properties of a book response object")
public class BookResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String author;
    private String genre;
    private String title;
    private String description;
    private int copiesAvailable;
    private int numberOfPages;
    private String coverImage;

}