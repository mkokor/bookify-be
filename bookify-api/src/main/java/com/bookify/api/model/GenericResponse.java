package com.bookify.api.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Properties of a message wrapper response object")
public class GenericResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String message;

}