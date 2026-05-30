package com.uit.se109.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "provinces")
public class Province extends BaseEntity {
  @Column(nullable = false, unique = true)
  private String code;

  @Column(nullable = false)
  private String name;

  private String type;
}
