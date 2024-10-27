package com.bookify.api.model.confirmationcode;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Properties of an email confirmation code update object")
public class EmailConfirmationCodeUpdateRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Schema(description = "Email of the user")
  @NotBlank(message = "Email must not be blank.")
  @Email(message = "Email address is not valid.")
  private String userEmail;

}