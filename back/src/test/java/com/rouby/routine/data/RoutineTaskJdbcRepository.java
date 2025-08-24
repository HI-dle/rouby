package com.rouby.routine.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.routine.routine_task.domain.RoutineTask;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class RoutineTaskJdbcRepository {

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  public RoutineTaskJdbcRepository(JdbcTemplate jdbcTemplate, ObjectMapper objectMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.objectMapper = objectMapper;
  }

  public List<Long> fetchNextRoutineTaskIds(int count) {
    return jdbcTemplate.queryForList(
        "SELECT nextval('routine_tasks_id_seq') FROM generate_series(1, ?)",
        Long.class, count);
  }

  public void batchInsertRoutineTasks(List<RoutineTask> routineTasks, List<Long> ids) {
    String sql = """
        INSERT INTO routine_tasks (
            id, user_id, title, task_type, target_value,
            start_date, until, time, weekdays,
            recurrence_rule, alarm_offset_type,
            override_type, override_date,
            created_at, created_by
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?::jsonb, ?::jsonb, ?, ?, ?, ?, ?)
        """;

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        RoutineTask task = routineTasks.get(i);
        Long id = ids.get(i);

        ps.setLong(1, id);
        ps.setLong(2, task.getUserId());
        ps.setString(3, task.getTitle());
        ps.setString(4, task.getTaskType().name()); // String으로 설정
        ps.setInt(5, task.getTargetValue());

        // RoutineTimeInfo
        ps.setDate(6, java.sql.Date.valueOf(task.getRoutineTimeInfo().getStartDate()));
        ps.setDate(7, java.sql.Date.valueOf(task.getRoutineTimeInfo().getUntil()));
        ps.setTime(8, java.sql.Time.valueOf(task.getRoutineTimeInfo().getTime()));

        // JSON 변환
        try {
          // weekdays JSON
          List<String> weekdayNames = task.getRoutineTimeInfo().getWeekdays().stream()
              .map(Enum::name)
              .collect(Collectors.toList());
          ps.setString(9, objectMapper.writeValueAsString(weekdayNames));

          // recurrenceRule JSON
          Map<String, Object> rruleMap = new HashMap<>();
          if (task.getRecurrenceRule() != null) {
            rruleMap.put("freq", task.getRecurrenceRule().getFreq().name());
            rruleMap.put("interval", task.getRecurrenceRule().getInterval());

            if (task.getRecurrenceRule().getByDay() != null) {
              List<String> byDayList = task.getRecurrenceRule().getByDay().stream()
                  .map(Enum::name).collect(Collectors.toList());
              try {
                rruleMap.put("by_day", objectMapper.writeValueAsString(byDayList));
              } catch (JsonProcessingException e) {
                throw new RuntimeException("byDay 직렬화 실패", e);
              }
            }
            if (task.getRecurrenceRule().getUntil() != null) {
              rruleMap.put("until", task.getRecurrenceRule().getUntil().toString());
            }
          }
          ps.setString(10, objectMapper.writeValueAsString(rruleMap));

        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }

        // AlarmOffsetType - String으로 설정
        ps.setString(11, task.getAlarmOffsetType() != null ?
            task.getAlarmOffsetType().name() : null);

        // OverrideInfo - String으로 설정
        ps.setString(12, task.getOverrideInfo() != null ?
            task.getOverrideInfo().getOverrideType().name() : null);
        ps.setDate(13, task.getOverrideInfo() != null ?
            java.sql.Date.valueOf(task.getOverrideInfo().getOverrideDate()) : null);

        // Audit fields
        ps.setTimestamp(14, Timestamp.valueOf(LocalDateTime.now()));
        ps.setLong(15, task.getUserId()); // created_by
      }

      @Override
      public int getBatchSize() {
        return routineTasks.size();
      }
    });
  }
}