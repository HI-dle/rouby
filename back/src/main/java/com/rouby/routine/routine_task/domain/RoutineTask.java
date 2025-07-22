package com.rouby.routine.routine_task.domain;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.routine.routine_task.domain.enums.AlarmOffsetType;
import com.rouby.routine.routine_task.domain.enums.OverrideType;
import com.rouby.routine.routine_task.domain.enums.TaskType;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "routine_tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RoutineTask extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "title", length = 500, nullable = false)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(name = "task_type", length = 10, nullable = false)
  private TaskType taskType;

  @Column(name = "target_value", nullable = false)
  private Integer targetValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "alarm_offset_type", length = 10)
  private AlarmOffsetType alarmOffsetType;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "recurrenceRule", columnDefinition = "jsonb", nullable = false)
  private RecurrenceRule recurrenceRule;

  @Embedded
  private RoutineTimeInfo routineTimeInfo;

  @Enumerated(EnumType.STRING)
  @Column(name = "override_type", length = 10)
  private OverrideType overrideType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_routine_task_id")
  private RoutineTask parentRoutineTask;

  @Builder
  private RoutineTask(AlarmOffsetType alarmOffsetType, OverrideType overrideType,
      RoutineTask parentRoutineTask, RecurrenceRule recurrenceRule, RoutineTimeInfo routineTimeInfo,
      Integer targetValue, TaskType taskType, String title, Long userId) {
    validate(title, targetValue, taskType, routineTimeInfo);
    this.alarmOffsetType = alarmOffsetType;
    this.overrideType = overrideType;
    this.parentRoutineTask = parentRoutineTask;
    this.recurrenceRule = recurrenceRule;
    this.routineTimeInfo = routineTimeInfo;
    this.targetValue = targetValue;
    this.taskType = taskType;
    this.title = title;
    this.userId = userId;
  }

  private void validate(String title, Integer targetValue, TaskType taskType, RoutineTimeInfo timeInfo) {
    if (title == null || title.trim().isEmpty()) {
      throw new IllegalArgumentException("제목은 필수입니다.");
    }

    if (taskType == null) {
      throw new IllegalArgumentException("taskType은 필수입니다.");
    }

    if (taskType != TaskType.CHECK && (targetValue == null || targetValue < 0)) {
      throw new IllegalArgumentException("targetValue는 0 이상의 값이어야 합니다.");
    }

    if (timeInfo == null) {
      throw new IllegalArgumentException("routineTimeInfo는 필수입니다.");
    }

  }
}