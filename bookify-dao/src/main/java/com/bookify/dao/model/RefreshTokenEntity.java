package com.bookify.dao.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity extends Auditable implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private String value;

  @Column(name = "expiration_date", nullable = false, updatable = false)
  private LocalDateTime expirationDate;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private UserEntity user;

}