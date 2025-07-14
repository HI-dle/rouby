package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.Weekday;
import java.time.ZonedDateTime;
import java.util.List;

public class RecurrenceRule {

  private List<Weekday> byDays;
  private ZonedDateTime until;

  public RecurrenceRule(List<Weekday> byDays, ZonedDateTime until) {
    this.byDays = byDays;
    this.until = until;
  }

  public List<Weekday> getByDays() {
    return byDays;
  }

  public ZonedDateTime getUntil() {
    return until;
  }

  public String toRRule() {
    return RRuleUtil.buildRRule(byDays, until);
  }

  public static RecurrenceRule fromRRule(String rrule) {
    return RRuleUtil.parseRRule(rrule);
  }
}

