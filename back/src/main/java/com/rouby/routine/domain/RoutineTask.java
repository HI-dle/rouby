package com.rouby.routine.domain;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.routine.domain.enums.TaskType;
import com.rouby.routine.domain.enums.AlarmOffsetType;
import com.rouby.routine.domain.enums.OverrideType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "routine_tasks")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
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

  @Column(name = "target_value")
  private Integer targetValue;

  @Enumerated(EnumType.STRING)
  @Column(name = "alarm_offset_type", length = 10)
  private AlarmOffsetType alarmOffsetType;

  @Column(name = "recurrence_rule", length = 1000)
  private String recurrenceRule;

  @Embedded
  private RoutineTimeInfo routineTimeInfo;

  @Enumerated(EnumType.STRING)
  @Column(name = "override_type", length = 10)
  private OverrideType overrideType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_routine_task_id")
  private RoutineTask parentRoutineTask;


}