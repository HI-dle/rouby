package com.rouby.routine.routine_task.domain;

import com.rouby.common.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyTask extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "routine_task_id", nullable = false)
  private RoutineTask routineTask;

  @Column(name = "current_value", nullable = false)
  private Integer currentValue;

  @Column(name = "task_date", nullable = false)
  private LocalDate taskDate;

  @Builder
  public DailyTask(Integer currentValue, RoutineTask routineTask, LocalDate taskDate) {
    this.currentValue = currentValue;
    this.routineTask = routineTask;
    this.taskDate = taskDate;
  }
}
