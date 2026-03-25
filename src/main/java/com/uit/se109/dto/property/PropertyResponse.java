package com.uit.se109.dto.property;

import com.uit.se109.entities.BaseEntity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyResponse extends BaseEntity {
  private String title;
  private String purpose;
  private String type;
  private BigDecimal price;
  private String lineAddress;
  private BigDecimal landArea;
  private BigDecimal floorArea;
  private Integer floors;
  private Integer bedrooms;
  private Integer bathrooms;
  private String direction;
  private String balconyDirection;
  private String interior;
  private String description;
  private String status;
}
