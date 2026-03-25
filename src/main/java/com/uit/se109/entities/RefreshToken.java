package com.uit.se109.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  private String token;

  private Instant expiryAt;
}
