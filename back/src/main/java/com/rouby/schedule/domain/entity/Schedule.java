package com.rouby.schedule.domain.entity;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.schedule.domain.enums.OverrideType;
import com.rouby.schedule.domain.vo.Period;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(length = 500, nullable = false)
  private String title;

  @Column(columnDefinition = "TEXT")
  private String memo;

  private LocalDate routineActivateDate;

  private Integer alarm_offset_minutes;

  @Embedded
  private Period period;

  @Column(length = 1000)
  private String recurrenceRule;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "parent_schedule_id")
  private Schedule parentSchedule;

  @OneToMany(mappedBy = "parentSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Schedule> children;

  @Enumerated(EnumType.STRING)
  private OverrideType overrideType;

  @Builder
  public Schedule(Long userId, String title, String memo,
      LocalDate routineActivateDate, Integer alarm_offset_minutes,
      LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime, String recurrenceRule) {

    this.userId = userId;
    this.title = title;
    this.memo = memo;
    this.routineActivateDate = routineActivateDate;
    this.alarm_offset_minutes = alarm_offset_minutes;
    this.period = new Period(startDate, startTime, endDate, endTime);
    this.recurrenceRule = recurrenceRule;
  }

  public void relateParentSchedule(Schedule schedule) {
    this.parentSchedule = schedule;
  }
}
