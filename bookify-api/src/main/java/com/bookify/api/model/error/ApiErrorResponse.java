package com.bookify.api.model.error;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Properties of an error response object")
public class ApiErrorResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private List<ApiError> errors;

}