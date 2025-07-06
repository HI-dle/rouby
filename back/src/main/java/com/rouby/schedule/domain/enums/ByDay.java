package com.rouby.schedule.domain.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

public enum ByDay {
  SU,
  MO,
  TU,
  WE,
  TH,
  FR,
  SA
  ;

  public static Set<ByDay> parseStringToSet(String byDayStr) {

    Set<ByDay> byDaySet = Collections.emptySet();

    if (byDayStr != null && !byDayStr.trim().isEmpty()) {
      try {
        byDaySet = Arrays.stream(byDayStr.split(","))
            .map(String::trim)
            .filter(day -> !day.isEmpty())
            .map(ByDay::valueOf)
            .collect(Collectors.toSet());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("잘못된 요일 값이 포함되어 있습니다: " + byDayStr, e);
      }
    }
    return byDaySet;
  }
}
