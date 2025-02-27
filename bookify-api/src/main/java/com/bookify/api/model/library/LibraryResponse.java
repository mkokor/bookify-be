package com.bookify.api.model.library;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Properties of a library response object")
public class LibraryResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String image;

}