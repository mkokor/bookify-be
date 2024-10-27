package com.bookify.api.validation.validator;

import com.bookify.api.validation.annotation.Username;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class UsernameValidator implements ConstraintValidator<Username, String> {

  @Override
  public boolean isValid(final String value, final ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return false;
    }

    final String usernameRegex = "^(?=(?:[^a-zA-Z]*[a-zA-Z]){2})(?!.*\\s)[a-zA-Z0-9]{3,}$";
    return Pattern.matches(usernameRegex, value.trim());
  }
  
}