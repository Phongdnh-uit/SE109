package com.uit.se109.dto.property;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PropertySummary {
  private Long id;
  private String title;
  private BigDecimal price;
  private String type;
  private String lineAddress;
  private BigDecimal landArea;
  private Integer bedrooms;
  private Integer bathrooms;
  private String status;
  // private String thumbnailUrl; // Thêm ảnh đại diện để hiển thị ở list
}
