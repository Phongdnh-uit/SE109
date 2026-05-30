package com.uit.se109.dto.property;

import com.uit.se109.enums.PropertyStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class PropertyRequest {
  @NotBlank private String title;
  private String purpose;
  private String type;

  @NotNull
  @DecimalMin(value = "0.0")
  private BigDecimal price;

  private String lineAddress;
  private Long wardId;
  private BigDecimal landArea;
  private BigDecimal floorArea;

  @Min(0)
  private Integer floors;

  private Integer floorNumber;
  private Integer bedrooms;
  private Integer bathrooms;
  private Double entranceRoadWidth;
  private String balconyDirection;
  private String direction;
  private String interior;
  private String description;
  @NotNull private PropertyStatus status;
}
