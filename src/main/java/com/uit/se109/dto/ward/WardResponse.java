package com.uit.se109.dto.ward;

import com.uit.se109.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WardResponse extends BaseEntity {
  private String code;
  private String name;
  private String type;
  private Long provinceId;
}
