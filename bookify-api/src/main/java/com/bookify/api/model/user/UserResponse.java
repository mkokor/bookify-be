package com.bookify.api.model.user;

import com.bookify.api.model.role.RoleResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Properties of a user response object")
public class UserResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID id;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  private Boolean emailConfirmed;
  private Boolean locked;
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private Set<RoleResponse> roles;

}