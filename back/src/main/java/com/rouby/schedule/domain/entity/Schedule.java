package com.rouby.schedule.domain.entity;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.schedule.domain.enums.AlarmOffsetType;
import com.rouby.schedule.domain.vo.OverrideInfo;
import com.rouby.schedule.domain.vo.Period;
import com.rouby.schedule.domain.vo.RecurrenceRule;
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
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "schedule", indexes = {
    @Index(name = "idx_schedule_user_start", columnList = "user_id, start_at"),
})
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

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private RecurrenceRule recurrenceRule;

  @OneToMany(mappedBy = "parentSchedule", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Schedule> children;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_schedule_id")
  private Schedule parentSchedule;

  @Embedded
  private OverrideInfo overrideInfo;

  @Builder
  private Schedule(Long userId, String title, String memo, Period period,
      LocalDate routineActivateDate, Integer alarmOffsetMinutes, RecurrenceRule recurrenceRule) {

    this.userId = userId;
    this.title = title;
    this.memo = memo;
    this.period = period;
    this.routineActivateDate = routineActivateDate;
    this.alarmOffsetType = alarmOffsetMinutes != null ? AlarmOffsetType.parse(alarmOffsetMinutes) : null;
    this.recurrenceRule = recurrenceRule;

    validate();
  }

  private void validate() {

    if (routineActivateDate != null) {
      if (!period.isValidRoutineActivateDate(routineActivateDate)) {
        throw new IllegalArgumentException("루틴 활성 일자가 일정보다 나중일 수 없습니다.");
      }
      if (isRoutineActivateDateOneMoreDayBefore()) {
        throw new IllegalArgumentException("루틴 활성 일자가 하루 이상 전일 수 없습니다.");
      }
    }

    // untilAt 이랑 endAt 비교 검증 수행하기 && (untilAt == null || untilAt.isAfter(endAt))

  }

  private void addToParentSchedule() {
    if (parentSchedule == null) return;
    this.parentSchedule.appendChildSchedule(this);
  }

  private void appendChildSchedule(Schedule schedule) {
    if (this.children == null) this.children = new ArrayList<>();
    this.children.add(schedule);
  }

  private boolean isRoutineActivateDateOneMoreDayBefore() {
    return routineActivateDate.isBefore(LocalDate.now().minusDays(1));
  }
}
