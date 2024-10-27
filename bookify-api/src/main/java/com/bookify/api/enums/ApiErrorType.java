package com.bookify.api.enums;

import lombok.Getter;

@Getter
public enum ApiErrorType {

  MODEL_VALIDATION("MODEL_VALIDATION"),
  BUSINESS_LOGIC("BUSINESS_LOGIC"),
  BAD_REQUEST("BAD_REQUEST"),
  INTERNAL_SERVER("INTERNAL_SERVER"),
  AUTHORIZATION("AUTHORIZATION");

  private final String value;

  ApiErrorType(final String value) {
    this.value = value;
  }

}