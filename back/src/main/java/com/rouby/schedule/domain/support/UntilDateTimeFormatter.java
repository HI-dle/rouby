package com.rouby.schedule.domain.support;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class UntilDateTimeFormatter {

  private static final ZoneId DEFAULT_ZONE = ZoneId.of("Asia/Seoul");

  public static ZonedDateTime parseZonedDateTime(String str) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern("yyyyMMdd'T'HHmmss")
        .optionalStart()
        .appendPattern("X")
        .optionalEnd()
        .toFormatter();

    if (str.matches(".*[Zz]|.*[+-]\\d{2}(:?\\d{2})?")) {
      return ZonedDateTime.parse(str, formatter);
    } else {
      return LocalDateTime.parse(str, formatter).atZone(DEFAULT_ZONE);
    }
  }

  public static LocalDateTime parse(String str) {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern("yyyyMMdd'T'HHmmss")
        .optionalStart()
        .optionalEnd()
        .toFormatter();

    return LocalDateTime.parse(str, formatter);
  }

  public static String format(ZonedDateTime until) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");
    return formatter.format(until);
  }

  public static String format(LocalDateTime until) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    return formatter.format(until);
  }
}
