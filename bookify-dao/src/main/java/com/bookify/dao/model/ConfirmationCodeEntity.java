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
@Table(name = "confirmation_codes")
public class ConfirmationCodeEntity extends Auditable implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  @Column(nullable = false)
  private String value;

  @Column(nullable = false)
  private String type;

  @Column(name = "expires_at", nullable = false)
  private LocalDateTime expiresAt;

  @ManyToOne
  @JoinColumn(name = "user_id", updatable = false)
  private UserEntity user;

}