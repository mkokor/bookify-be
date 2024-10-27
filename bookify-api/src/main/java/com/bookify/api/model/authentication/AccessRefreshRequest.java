package com.bookify.api.model.authentication;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccessRefreshRequest implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String refreshToken;

}