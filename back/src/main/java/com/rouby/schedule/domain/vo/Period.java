package com.rouby.schedule.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

  @Column(nullable = false)
  private LocalDate startDate;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalDate endDate;

  @Column(nullable = false)
  private LocalTime endTime;

  public Period(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {

    validate(startDate, startTime, endDate, endTime);

    this.startDate = startDate;
    this.startTime = startTime;
    this.endDate = endDate;
    this.endTime = endTime;
  }

  private void validate(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {

    boolean result = !startDate.isAfter(endDate) && !startTime.isAfter(endTime);
    Assert.isTrue(result, "시작일시가 종료일시보다 늦을 수 없습니다.");
  }
}
