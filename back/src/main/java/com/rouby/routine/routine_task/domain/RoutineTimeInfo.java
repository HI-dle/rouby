package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.Weekday;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "weekdays", columnDefinition = "jsonb", nullable = false)
  private List<Weekday> weekdays;

  @Builder
  public RoutineTimeInfo(LocalDate startDate, LocalDate endDate, LocalTime time, List<Weekday> weekdays) {
    validate(startDate, endDate);
    this.startDate = startDate;
    this.endDate = endDate;
    this.time = time;
    this.weekdays = weekdays;
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