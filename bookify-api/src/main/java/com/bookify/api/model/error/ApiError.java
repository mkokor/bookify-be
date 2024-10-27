package com.bookify.api.model.error;

import com.bookify.api.enums.ApiErrorType;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
public class ApiError implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String errorType;
  private String message;

  public ApiError(@NonNull final ApiErrorType errorType, final String message) {
    this.errorType = errorType.getValue();
    this.message = message;
  }

}