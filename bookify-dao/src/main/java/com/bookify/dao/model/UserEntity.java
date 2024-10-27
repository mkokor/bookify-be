package com.bookify.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Getter
@Setter
@Table(name = "users")
public class UserEntity extends Auditable implements UserDetails, Principal, Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private Boolean emailConfirmed;

  @Column(nullable = false)
  private Boolean locked;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(unique = true, nullable = false)
  private String username;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_role_map", joinColumns = {
      @JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private Set<RoleEntity> roles;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<ConfirmationCodeEntity> confirmationCodes;

  @OneToMany(mappedBy = "user")
  @JsonIgnore
  private List<RefreshTokenEntity> refreshTokens;

  @Override
  public String getName() {
    return username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return emailConfirmed;
  }

}