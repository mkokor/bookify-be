package com.bookify.api.model.authentication;

import com.bookify.api.validation.annotation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Properties of a password reset request object")
public class PasswordResetRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Email of the user")
  private String userEmail;

  @Schema(description = "Password reset code")
  @NotBlank(message = "Password reset code must be specified.")
  private String passwordResetCode;

  @Schema(description = "New password of the user")
  @Password
  private String newPassword;

}