package com.rouby.schedule.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.schedule.domain.entity.Schedule;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JdbcTestDataRepository {

  private final JdbcTemplate jdbcTemplate;
  private final ObjectMapper objectMapper;

  @Transactional
  public void batchInsert(int batchSize, List<Schedule> schedules) {

    var sql = """
        INSERT INTO schedule 
        (id, user_id, parent_schedule_id
        , title, memo, routine_offset_days, start_at, end_at
        , alarm_offset_type, override_type, override_date, recurrence_rule
        , created_at, created_by, updated_at, updated_by)
        VALUES
        (?, ?, ?
        , ?, ?, ?, ?, ?
        , ?, ?, ?, ?::jsonb
        , NOW(), ?, NOW(), ?)
        """;
    try {
        batchUpdateSchedules(sql, batchSize, schedules);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }

  private void batchUpdateSchedules(String sql, int batchSize, List<Schedule> schedules) {
    jdbcTemplate.batchUpdate(
        sql,
        schedules,
        batchSize,
        (ps, arg) -> {
          ps.setLong(1, arg.getId());
          ps.setLong(2, arg.getUserId());
          ps.setObject(3,
              arg.getParentSchedule() == null ? null : arg.getParentSchedule().getId(),
              Types.BIGINT);
          ps.setString(4, arg.getTitle());
          ps.setString(5, arg.getMemo());
          ps.setInt(6, arg.getRoutineOffsetDays());
          ps.setTimestamp(7, Timestamp.valueOf(arg.getPeriod().getStartAt()));
          ps.setTimestamp(8, Timestamp.valueOf(arg.getPeriod().getEndAt()));
          ps.setObject(9,
              arg.getAlarmOffsetType() == null ? null : arg.getAlarmOffsetType().name(),
              Types.VARCHAR);
          ps.setObject(10,
              arg.getOverrideInfo() == null ? null : arg.getOverrideInfo().getOverrideType().name(),
              Types.VARCHAR);
          ps.setObject(11,
              arg.getOverrideInfo() == null ? null : Date.valueOf(
                  arg.getOverrideInfo().getOverrideDate()),
              Types.DATE);
          ps.setObject(12,
              arg.getRecurrenceRule() == null
                  ? null
                  : toJsonString(arg.getRecurrenceRule())
                      .replaceAll("\"byDay\":", "\"by_day\":"),
              Types.VARCHAR);
          ps.setLong(13, arg.getUserId());
          ps.setLong(14, arg.getUserId());
        });
  }

  public List<Long> fetchNextIds(int count) {
    String sql = "SELECT nextval('schedule_seq') FROM generate_series(1, ?)";
    return jdbcTemplate.query(
        sql,
        ps -> {
          ps.setInt(1, count);
        },
        (rs, rowNum) -> rs.getLong(1)
    );
  }

  private String toJsonString(Object object) {

    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
