package com.bookify.api.enums;

import lombok.Getter;

@Getter
public enum ConfirmationCodeType {

  EMAIL_CONFIRMATION("Email Confirmation"),
  PASSWORD_RESET("Password Reset");

  private final String value;

  ConfirmationCodeType(final String value) {
    this.value = value;
  }

}