package com.bookify.api.validation.annotation;

import com.bookify.api.validation.validator.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = PasswordValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Password {

  public String message() default "Password must contain at least 8 characters, including one digit and one special character.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}