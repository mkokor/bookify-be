package com.bookify.api.model.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Properties of a role response object")
public class RoleResponse implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private UUID id;
  private String name;

}