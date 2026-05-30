package com.uit.se109.dto.ward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WardRequest {
  @NotBlank private String code;
  @NotBlank private String name;
  private String type;
  @NotNull private Long provinceId;
}
