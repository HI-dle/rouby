package com.rouby.schedule.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

  @Column(nullable = false)
  private LocalDateTime startAt;

  @Column(nullable = false)
  private LocalDateTime endAt;

  @Column
  private LocalDateTime untilAt;

  @Builder
  private Period(LocalDateTime startAt, LocalDateTime endAt, LocalDateTime untilAt) {

    validate(startAt, endAt, untilAt);
    this.startAt = startAt;
    this.endAt = endAt;
    this.untilAt = untilAt;
  }

  private void validate(LocalDateTime startAt, LocalDateTime endAt, LocalDateTime untilAt) {

    boolean result = !startAt.isAfter(endAt) && (untilAt == null || untilAt.isAfter(endAt));
    Assert.isTrue(result, "시작일시가 종료일시보다 늦을 수 없습니다.");
  }

  public boolean isValidRoutineActivateDate(LocalDate routineActivateDate) {
    return !routineActivateDate.isAfter(endAt.toLocalDate());
  }
}
