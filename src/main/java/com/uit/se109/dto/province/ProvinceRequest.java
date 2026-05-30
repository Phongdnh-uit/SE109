package com.uit.se109.dto.province;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProvinceRequest {
  @NotBlank private String code;
  @NotBlank private String name;
  private String type;
}
