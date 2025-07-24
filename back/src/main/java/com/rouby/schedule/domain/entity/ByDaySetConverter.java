package com.rouby.schedule.domain.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.common.jpa.EnumSetConverter;
import com.rouby.schedule.domain.enums.ByDay;
import org.springframework.stereotype.Component;

@Component
public class ByDaySetConverter extends EnumSetConverter<ByDay> {
  public ByDaySetConverter(ObjectMapper objectMapper) {
    super(ByDay.class, objectMapper);
  }
}