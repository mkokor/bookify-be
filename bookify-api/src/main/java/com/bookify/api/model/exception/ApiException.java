package com.bookify.api.model.exception;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends Exception implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private final HttpStatus statusCode;

  public ApiException(final HttpStatus statusCode, final String message) {
    super(message);
    this.statusCode = statusCode;
  }

}