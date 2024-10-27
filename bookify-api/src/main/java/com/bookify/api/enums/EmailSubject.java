package com.bookify.api.enums;

import lombok.Getter;

@Getter
public enum EmailSubject {

  EMAIL_CONFIRMATION("Email Confirmation"),
  PASSWORD_RESET("Password Reset");

  private final String value;

  EmailSubject(final String value) {
    this.value = value;
  }

}