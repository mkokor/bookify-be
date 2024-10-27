package com.bookify.api.validation.annotation;

import com.bookify.api.validation.validator.UsernameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Constraint(validatedBy = UsernameValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Username {

  String message() default "Username must not contain whitespace characters and must have at least three characters, of which at least two are letters.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

}