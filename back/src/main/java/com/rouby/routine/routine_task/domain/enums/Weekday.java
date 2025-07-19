package com.rouby.routine.routine_task.domain.enums;

import java.time.DayOfWeek;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Weekday {
  MO(DayOfWeek.MONDAY, "월"),
  TU(DayOfWeek.TUESDAY, "화"),
  WE(DayOfWeek.WEDNESDAY, "수"),
  TH(DayOfWeek.THURSDAY, "목"),
  FR(DayOfWeek.FRIDAY, "금"),
  SA(DayOfWeek.SATURDAY, "토"),
  SU(DayOfWeek.SUNDAY, "일");

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

}
