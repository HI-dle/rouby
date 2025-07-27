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
  public void batchInsert(int batchSize, List<Schedule> list) {

    var sql = """
        INSERT INTO schedule 
        (user_id, parent_schedule_id
        , title, memo, routine_offset_days, start_at, end_at
        , alarm_offset_type, override_type, override_date, recurrence_rule
        , created_at, created_by, updated_at, updated_by)
        VALUES
        (?, ?
        , ?, ?, ?, ?, ?
        , ?, ?, ?, ?::jsonb
        , NOW(), ?, NOW(), ?)
        """;

    try {
      jdbcTemplate.batchUpdate(
          sql,
          list,
          batchSize,
          (ps, arg) -> {
            ps.setLong(1, arg.getUserId());
            ps.setObject(2,
                arg.getParentSchedule() == null ? null : arg.getParentSchedule().getId(),
                Types.BIGINT);
            ps.setString(3, arg.getTitle());
            ps.setString(4, arg.getMemo());
            ps.setInt(5, arg.getRoutineOffsetDays());
            ps.setTimestamp(6, Timestamp.valueOf(arg.getPeriod().getStartAt()));
            ps.setTimestamp(7, Timestamp.valueOf(arg.getPeriod().getEndAt()));
            ps.setObject(8,
                arg.getAlarmOffsetType() == null ? null : arg.getAlarmOffsetType().name(),
                Types.VARCHAR);
            ps.setObject(9,
                arg.getOverrideInfo() == null ? null : arg.getOverrideInfo().getOverrideType().name(),
                Types.VARCHAR);
            ps.setObject(10,
                arg.getOverrideInfo() == null ? null : Date.valueOf(
                    arg.getOverrideInfo().getOverrideDate()),
                Types.DATE);
            ps.setObject(11,
                arg.getRecurrenceRule() == null
                    ? null
                    : toJsonString(arg.getRecurrenceRule())
                        .replaceAll("\"byDay\":", "\"by_day\":"),
                Types.VARCHAR);
            ps.setLong(12, arg.getUserId());
            ps.setLong(13, arg.getUserId());
          });

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private String toJsonString(Object object) {

    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
