package com.bookify.api.model.authentication;

import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetCodeRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @NotNull(message = "User email must be provided.")
  private String userEmail;

}