package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.Weekday;
import java.time.ZonedDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RecurrenceRule {

  private List<Weekday> byDays;
  private ZonedDateTime until;

}

