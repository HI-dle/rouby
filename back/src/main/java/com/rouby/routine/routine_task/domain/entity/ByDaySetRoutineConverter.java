package com.rouby.routine.routine_task.domain.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.common.jpa.EnumSetConverter;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import org.springframework.stereotype.Component;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 01.
 */
@Component
public class ByDaySetRoutineConverter extends EnumSetConverter<Weekday> {
  public ByDaySetRoutineConverter(ObjectMapper objectMapper) {
    super(Weekday.class, objectMapper);
  }
}
