package com.rouby.schedule.domain.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum AlarmOffsetType {

  M_5(5, "5분 전"),
  M_10(10, "10분 전"),
  M_15(15, "15분 전"),
  M_30(30, "30분 전"),
  H_1(60, "1시간 전"),
  H_2(120, "2시간 전"),
  D_1(1440, "1일 전"),
  D_2(2880, "2일 전"),
  W_1(10080, "일주일 전"),
  ;

  private final Integer minutes;
  private final String desc;
}
