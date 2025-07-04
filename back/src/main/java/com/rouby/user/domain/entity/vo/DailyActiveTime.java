package com.rouby.user.domain.entity.vo;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalTime;
import lombok.Getter;

@Getter
@Embeddable
public class DailyActiveTime implements Serializable {

  private LocalTime dailyStartTime;

  private LocalTime dailyEndTime;

  public static DailyActiveTime defaultTime() {
    return new DailyActiveTime(LocalTime.of(9, 0), LocalTime.of(22, 0));
  }

  public static DailyActiveTime of(LocalTime dailyStartTime, LocalTime dailyEndTime) {
    return new DailyActiveTime(dailyStartTime, dailyEndTime);
  }

  protected DailyActiveTime() {
  }

  private DailyActiveTime(LocalTime dailyStartTime, LocalTime dailyEndTime) {
    this.dailyStartTime = dailyStartTime;
    this.dailyEndTime = dailyEndTime;
  }
}
