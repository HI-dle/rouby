package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.Weekday;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;

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
