package com.rouby.schedule.domain.enums;

import java.util.function.Predicate;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum Freq {
  HOURLY(interval -> interval >= 1 && interval <= 24),
  DAILY(interval -> interval >= 1 && interval <= 30),
  WEEKLY(interval -> interval >= 1 && interval <= 12),
  MONTHLY(interval -> interval >= 1 && interval <= 12),
  YEARLY(interval -> interval >= 1 && interval <= 10),
  ;

  private final Predicate<Integer> intervalValidator;

  public void validateInterval(Integer interval) {
    if (interval == null || !intervalValidator.test(interval)) {
      throw new IllegalArgumentException(interval + "은" + this.name() +"반복에 부적절한 인터벌 값입니다.");
    }
  }
}
