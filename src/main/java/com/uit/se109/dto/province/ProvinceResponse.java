package com.uit.se109.dto.province;

import com.uit.se109.entities.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinceResponse extends BaseEntity {
  private String code;
  private String name;
  private String type;
}
