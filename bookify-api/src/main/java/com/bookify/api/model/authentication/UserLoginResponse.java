package com.bookify.api.model.authentication;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Properties of a user login response object")
public class UserLoginResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private String accessToken;
  private List<String> roles;
  private String username;

  @JsonIgnore
  private String refreshToken;

  @JsonIgnore
  private Integer refreshTokenExpirationSeconds;

}