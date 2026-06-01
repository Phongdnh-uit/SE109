package com.uit.se109.entities;

import com.uit.se109.enums.PropertyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "properties")
public class Property extends BaseEntity {
  @Column(nullable = false)
  private String title;

  private String purpose;

  private String type;

  @Column(nullable = false, precision = 15, scale = 2)
  private BigDecimal price;

  private String lineAddress;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ward_id")
  private Ward ward;

  @Column(precision = 10, scale = 2)
  private BigDecimal landArea;

  @Column(precision = 10, scale = 2)
  private BigDecimal floorArea;

  private Integer floors;

  private Integer floorNumber;

  private Integer bedrooms;

  private Integer bathrooms;

  private Double entranceRoadWidth;

  private String balconyDirection;

  private String direction;

  private String interior;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private PropertyStatus status;
}
