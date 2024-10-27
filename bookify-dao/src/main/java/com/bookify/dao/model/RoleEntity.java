package com.bookify.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class RoleEntity extends Auditable implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(nullable = false, updatable = false)
  private String name;

  @ManyToMany(mappedBy = "roles")
  @JsonIgnore
  private Set<UserEntity> users;

}