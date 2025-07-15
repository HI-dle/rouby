package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.Weekday;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.experimental.UtilityClass;

@Builder
@NoArgsConstructor
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

  public static RecurrenceRule from(String rrule) {
    return RRuleUtil.parseRRule(rrule);
  }

  @UtilityClass
  public class RRuleUtil {

    public static String buildRRule(List<Weekday> byDays, ZonedDateTime until) {
      String byDayString = byDays.stream()
          .map(Enum::name) // MO, TU, ...
          .collect(Collectors.joining(","));

      String untilStr = until.withZoneSameInstant(ZoneOffset.UTC)
          .format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));

      return String.format("FREQ=WEEKLY;BYDAY=%s;UNTIL=%s", byDayString, untilStr);
    }

    public static RecurrenceRule parseRRule(String rrule) {
      String[] parts = rrule.split(";");
      List<Weekday> byDays = null;
      ZonedDateTime until = null;

      for (String part : parts) {
        if (part.startsWith("BYDAY=")) {
          String daysStr = part.substring("BYDAY=".length());
          byDays = Arrays.stream(daysStr.split(","))
              .map(Weekday::valueOf)
              .collect(Collectors.toList());
        } else if (part.startsWith("UNTIL=")) {
          String untilStr = part.substring("UNTIL=".length());
          OffsetDateTime odt = OffsetDateTime.parse(untilStr, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX"));
          until = odt.toZonedDateTime();
        }
      }

      if (byDays == null || until == null) {
        throw new IllegalArgumentException("Invalid RRULE: " + rrule);
      }

      return new RecurrenceRule(byDays, until);
    }
  }
}

