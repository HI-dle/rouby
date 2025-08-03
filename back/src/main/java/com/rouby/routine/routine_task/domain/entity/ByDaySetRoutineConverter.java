package com.rouby.routine.routine_task.domain.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.common.jpa.EnumSetConverter;
import com.rouby.schedule.domain.enums.ByDay;
import org.springframework.stereotype.Component;

/**
 * @author : hanjihoon
 * @Date : 2025. 08. 01.
 */
@Component
public class ByDaySetRoutineConverter extends EnumSetConverter<ByDay> {
  public ByDaySetRoutineConverter(ObjectMapper objectMapper) {
    super(ByDay.class, objectMapper);
  }
}
