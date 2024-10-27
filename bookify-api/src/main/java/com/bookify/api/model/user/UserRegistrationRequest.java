package com.bookify.api.model.user;

import com.bookify.api.validation.annotation.Password;
import com.bookify.api.validation.annotation.Username;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Properties of a user registration request object")
public class UserRegistrationRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "First name of the user")
  @NotBlank(message = "First name must not be blank.")
  private String firstName;

  @Schema(description = "Last name of the user")
  @NotBlank(message = "Last name must not be blank.")
  private String lastName;

  @Schema(description = "Username of the user")
  @NotBlank(message = "Username must not be blank.")
  @Username
  private String username;

  @Schema(description = "Email of the user")
  @NotBlank(message = "Email must not be blank.")
  @Email(message = "Email address is not valid.")
  private String email;

  @Schema(description = "Password to be set on user's account")
  @Password
  private String password;

}