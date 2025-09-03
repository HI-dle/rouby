package com.rouby.routine.data;

import com.rouby.routine.daily_task.domain.DailyTask;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

public class DailyTaskJdbcRepository {

  private final JdbcTemplate jdbcTemplate;

  public DailyTaskJdbcRepository(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  public void batchInsertDailyTasks(List<DailyTask> dailyTasks) {
    String sql = """
        INSERT INTO daily_tasks (
            id, routine_task_id, task_date, current_value, created_at, created_by
        ) VALUES (DEFAULT, ?, ?, ?, now(), ?)
        ON CONFLICT (routine_task_id, task_date) DO NOTHING
        """;

    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        DailyTask task = dailyTasks.get(i);

        ps.setLong(1, task.getRoutineTaskId());
        ps.setDate(2, java.sql.Date.valueOf(task.getTaskDate()));
        ps.setInt(3, task.getCurrentValue());
        ps.setLong(4, 1L);
      }

      @Override
      public int getBatchSize() {
        return dailyTasks.size();
      }
    });
  }
}