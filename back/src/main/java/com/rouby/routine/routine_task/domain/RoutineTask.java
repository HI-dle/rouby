package com.rouby.routine.routine_task.domain;

import com.rouby.common.jpa.BaseEntity;
import com.rouby.routine.routine_task.domain.enums.TaskType;
import com.rouby.routine.routine_task.domain.enums.AlarmOffsetType;
import com.rouby.routine.routine_task.domain.enums.OverrideType;
import com.rouby.routine.routine_task.domain.enums.Weekday;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

  @Convert(converter = RRuleConverter.class)
  private RecurrenceRule recurrenceRule;

  @Embedded
  private RoutineTimeInfo routineTimeInfo;

  @Enumerated(EnumType.STRING)
  @Column(name = "override_type", length = 10)
  private OverrideType overrideType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_routine_task_id")
  private RoutineTask parentRoutineTask;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "weekdays", columnDefinition = "jsonb", nullable = false)
  private List<Weekday> weekdays;
}