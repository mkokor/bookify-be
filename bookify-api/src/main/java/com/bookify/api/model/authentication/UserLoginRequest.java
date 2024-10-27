package com.bookify.api.model.authentication;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Properties of a user login request object")
public class UserLoginRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Username of the user")
  @NotBlank(message = "Username must be specified.")
  private String username;

  @Schema(description = "Password of the user")
  @NotBlank(message = "Password must be specified.")
  private String password;

}