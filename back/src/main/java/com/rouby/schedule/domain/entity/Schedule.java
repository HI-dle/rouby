package com.rouby.schedule.domain.entity;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.schedule.domain.enums.AlarmOffsetType;
import com.rouby.schedule.domain.enums.OverrideType;
import com.rouby.schedule.domain.vo.Period;
import com.rouby.schedule.domain.vo.RecurrenceRule;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

  @Embedded
  private Period period;

  private LocalDate routineActivateDate;

  @Enumerated(EnumType.STRING)
  private AlarmOffsetType alarmOffsetType;

  @Column(columnDefinition = "TEXT")
  @Convert(converter = RecurrenceRuleStringConverter.class)
  private RecurrenceRule recurrenceRule;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_schedule_id")
  private Schedule parentSchedule;

  @OneToMany(mappedBy = "parentSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Schedule> children;

  @Enumerated(EnumType.STRING)
  private OverrideType overrideType;

  private LocalDate overrideDate;

  @Builder
  public Schedule(Long userId, String title, String memo,
      LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime,
      LocalDate routineActivateDate, Integer alarmOffsetMinutes, RecurrenceRule recurrenceRule) {

    this.userId = userId;
    this.title = title;
    this.memo = memo;
    this.period = new Period(startDate, startTime, endDate, endTime);
    this.routineActivateDate = routineActivateDate;
    this.alarmOffsetType = AlarmOffsetType.parse(alarmOffsetMinutes);
    this.recurrenceRule = recurrenceRule;

    validate();
  }

  private void validate() {

    if (!period.isValidRoutineActivateDate(routineActivateDate)) {
      throw new IllegalArgumentException("루틴 활성 일자가 일정보다 나중일 수 없습니다.");
    }
  }

  public void assignUserId(Long userId) {
    this.userId = userId;
  }

  public void relateParentSchedule(Schedule schedule) {
    this.parentSchedule = schedule;
    this.parentSchedule.children.add(this);
  }
}
