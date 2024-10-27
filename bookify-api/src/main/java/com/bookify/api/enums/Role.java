package com.bookify.api.enums;

import lombok.Getter;

@Getter
public enum Role {

  CUSTOMER("Customer"),
  EMPLOYEE("Employee");

  private final String value;

  Role(final String value) {
    this.value = value;
  }

}