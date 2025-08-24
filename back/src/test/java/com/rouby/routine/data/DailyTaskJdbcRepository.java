package com.rouby.routine.data;

import com.rouby.routine.daily_task.domain.DailyTask;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class DailyTaskJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public DailyTaskJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public List<Long> fetchNextDailyTaskIds(int count) {
    return jdbcTemplate.queryForList(
        "SELECT nextval('daily_tasks_id_seq') FROM generate_series(1, ?)",
        Long.class, count);
  }

  public void batchInsertDailyTasks(List<DailyTask> dailyTasks) {
    String sql = """
        INSERT INTO daily_tasks (
            id, routine_task_id, task_date, current_value, created_at, created_by
        ) VALUES (?, ?, ?, ?, ?, ?)
        ON CONFLICT (routine_task_id, task_date) DO NOTHING
        """;

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        DailyTask task = dailyTasks.get(i);
        Long generatedId = jdbcTemplate.queryForObject(
            "SELECT nextval('daily_tasks_id_seq')", Long.class);

        ps.setLong(1, generatedId);
        ps.setLong(2, task.getRoutineTaskId());
        ps.setDate(3, java.sql.Date.valueOf(task.getTaskDate()));
        ps.setInt(4, task.getCurrentValue());
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setLong(6, 1L); // created_by
      }

      @Override
      public int getBatchSize() {
        return dailyTasks.size();
      }
    });
  }
}