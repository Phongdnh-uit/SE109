package com.uit.se109.dto.province;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProvinceSummary {
  private Long id;
  private String code;
  private String name;
  private String type;
}
