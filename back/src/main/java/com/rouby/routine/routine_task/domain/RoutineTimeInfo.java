package com.rouby.routine.routine_task.domain;

import com.rouby.routine.routine_task.domain.enums.Weekday;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

  @Column(name = "until", nullable = false)
  private LocalDate until;

  @Column(name = "time", nullable = false)
  private LocalTime time;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "weekdays", columnDefinition = "jsonb", nullable = false)
  private List<Weekday> weekdays;

  @Builder
  private RoutineTimeInfo(LocalDate startDate, LocalDate until, LocalTime time, List<Weekday> weekdays) {
    validate(startDate, until, time, weekdays);
    this.startDate = startDate;
    this.until = until;
    this.time = time;
    this.weekdays = weekdays;
  }

  private void validate(LocalDate startDate, LocalDate until, LocalTime time, List<Weekday> weekdays) {
    if (startDate == null) {
      throw new IllegalArgumentException("startDate는 필수입니다.");
    }

    if (time == null) {
      throw new IllegalArgumentException("시간은 필수입니다.");
    }

    if (until == null) {
      throw new IllegalArgumentException("until은 필수입니다.");
    }

    if (until.isBefore(startDate)) {
      throw new IllegalArgumentException("endDate는 startDate보다 이전일 수 없습니다.");
    }

    validateWeekdays(startDate, until, weekdays);
  }

  private void validateWeekdays(LocalDate startDate, LocalDate until, List<Weekday> weekdays) {
    if (weekdays == null || weekdays.isEmpty()) {
      throw new IllegalArgumentException("반복 요일은 1개 이상 선택해야 합니다.");
    }

    long daysBetween = ChronoUnit.DAYS.between(startDate, until);

    if (daysBetween < 6) {
      Set<DayOfWeek> validDays = startDate
          .datesUntil(until.plusDays(1))
          .map(LocalDate::getDayOfWeek)
          .collect(Collectors.toSet());

      List<Weekday> invalid = weekdays.stream()
          .filter(w -> !validDays.contains(w.getDayOfWeek()))
          .toList();

      if (!invalid.isEmpty()) {
        String invalidStr = invalid.stream()
            .map(Weekday::getKoreanName)
            .collect(Collectors.joining(", "));
        throw new IllegalArgumentException("선택된 요일 중 [" + invalidStr + "] 은(는) 기간 내에 포함되지 않습니다.");
      }
    }
  }
}