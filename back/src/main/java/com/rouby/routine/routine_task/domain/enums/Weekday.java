package com.rouby.routine.routine_task.domain.enums;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Weekday {
  SU(DayOfWeek.SUNDAY, "일"),
  MO(DayOfWeek.MONDAY, "월"),
  TU(DayOfWeek.TUESDAY, "화"),
  WE(DayOfWeek.WEDNESDAY, "수"),
  TH(DayOfWeek.THURSDAY, "목"),
  FR(DayOfWeek.FRIDAY, "금"),
  SA(DayOfWeek.SATURDAY, "토"),
  ;

  private final DayOfWeek dayOfWeek;
  private final String koreanName;

  public DayOfWeek getDayOfWeek() {
    return dayOfWeek;
  }

  public String getKoreanName() {
    return koreanName;
  }

  public static Weekday from(DayOfWeek dayOfWeek) {
    return Arrays.stream(values())
        .filter(w -> w.dayOfWeek == dayOfWeek)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 요일: " + dayOfWeek));
  }
  public static Set<Weekday> parseStringToSet(String byDayStr) {

    if (byDayStr != null && !byDayStr.isBlank()) {
      try {
        return Arrays.stream(byDayStr.split(","))
            .map(String::trim)
            .filter(day -> !day.isEmpty())
            .map(Weekday::valueOf)
            .collect(Collectors.toSet());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("잘못된 요일 값이 포함되어 있습니다: " + byDayStr, e);
      }
    }
    return Collections.emptySet();
  }

}
