package com.bookify.api.model.exception;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InternalServerException extends ApiException implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  public InternalServerException(final String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

}