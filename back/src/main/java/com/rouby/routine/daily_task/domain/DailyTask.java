package com.rouby.routine.daily_task.domain;

import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_tasks",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_routine_task_date",
            columnNames = {"routine_task_id", "task_date"}
        )
    })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyTask extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "routine_task_id", nullable = false)
  private Long routineTaskId;

  @Column(name = "current_value", nullable = false)
  private Integer currentValue;

  @Column(name = "task_date", nullable = false)
  private LocalDate taskDate;

  @Builder
  public DailyTask(Integer currentValue, Long routineTaskId, LocalDate taskDate) {
    this.currentValue = currentValue;
    this.routineTaskId = routineTaskId;
    this.taskDate = taskDate;
  }

  public static DailyTask create(Long routineTaskId, LocalDate taskDate, Integer currentValue) {
    validateRequirements(routineTaskId, taskDate);
    validateCurrentValue(currentValue);

    return DailyTask.builder()
        .routineTaskId(routineTaskId)
        .taskDate(taskDate)
        .currentValue(currentValue)
        .build();
  }

  private static void validateRequirements(Long routineTaskId, LocalDate taskDate) {
    if (routineTaskId == null) {
      throw new IllegalArgumentException("루틴 태스크 ID는 필수입니다.");
    }
    if (taskDate == null) {
      throw new IllegalArgumentException("태스크 날짜는 필수입니다.");
    }
  }

  private static void validateCurrentValue(Integer currentValue) {
    if (currentValue == null) {
      throw new IllegalArgumentException("진행률 값은 필수입니다.");
    }
    if (currentValue < 0) {
      throw new IllegalArgumentException("진행률 값은 0 이상이어야 합니다.");
    }
  }

  public void update(Long routineTaskId, LocalDate taskDate, Integer newValue) {
    validateRoutineIdentity(routineTaskId, taskDate);
    validateCurrentValue(newValue);

    this.currentValue = newValue;
  }

  private void validateRoutineIdentity(Long routineTaskId, LocalDate taskDate) {
    validateRequirements(routineTaskId, taskDate);
    if (!this.routineTaskId.equals(routineTaskId) || !this.taskDate.equals(taskDate)) {
      throw new IllegalArgumentException("요청한 routineTaskId와 taskDate가 해당 DailyTask와 일치하지 않습니다.");
    }
  }
}
