package com.rouby.routine.routine_task.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoutineTimeInfo implements Serializable {

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "time", nullable = false)
  private LocalTime time;

  @Builder
  public RoutineTimeInfo(LocalDate startDate, LocalDate endDate, LocalTime time) {
    validate(startDate, endDate);
    this.startDate = startDate;
    this.endDate = endDate;
    this.time = time;
  }

  private void validate(LocalDate startDate, LocalDate endDate) {
    if (startDate == null) {
      throw new IllegalArgumentException("startDate는 필수입니다.");
    }

    if (endDate != null && endDate.isBefore(startDate)) {
      throw new IllegalArgumentException("endDate는 startDate보다 이전일 수 없습니다.");
    }
  }
}