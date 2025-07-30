package com.rouby.routine.daily_task.presentation;

import com.rouby.routine.daily_task.presentation.dto.ProgressDailyTaskRequest;
import java.time.LocalDate;

public class DailyTaskRequestFixture {

  static final Long VALID_ROUTINE_TASK_ID = 1L;
  static final Long INVALID_ROUTINE_TASK_ID = 0L;
  static final Long NOT_EXISTS_ROUTINE_TASK_ID = 999L;
  static final Long VALID_DAILY_TASK_ID = 123L;
  static final Long INVALID_DAILY_TASK_ID = 0L;
  static final Long NOT_EXISTS_DAILY_TASK_ID = 999L;
  static final Integer VALID_CURRENT_VALUE = 5;
  static final Integer UPDATED_CURRENT_VALUE = 8;
  static final Integer NEGATIVE_CURRENT_VALUE = -1;
  static final LocalDate VALID_TASK_DATE = LocalDate.of(2025, 7, 21);

  public static ProgressDailyTaskRequest createRequest() {
    return new ProgressDailyTaskRequest(
        null,
        VALID_ROUTINE_TASK_ID,
        VALID_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }

  public static ProgressDailyTaskRequest updateRequest() {
    return new ProgressDailyTaskRequest(
        VALID_DAILY_TASK_ID,
        VALID_ROUTINE_TASK_ID,
        UPDATED_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }

  public static ProgressDailyTaskRequest routineTaskNotExistsRequest() {
    return new ProgressDailyTaskRequest(
        null,
        NOT_EXISTS_ROUTINE_TASK_ID,
        VALID_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }

  public static ProgressDailyTaskRequest dailyTaskNotExistsRequest() {
    return new ProgressDailyTaskRequest(
        NOT_EXISTS_DAILY_TASK_ID,
        VALID_ROUTINE_TASK_ID,
        VALID_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }

  public static ProgressDailyTaskRequest negativeValueRequest() {
    return new ProgressDailyTaskRequest(
        null,
        VALID_ROUTINE_TASK_ID,
        NEGATIVE_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }

  public static ProgressDailyTaskRequest invalidIdRequest() {
    return new ProgressDailyTaskRequest(
        INVALID_DAILY_TASK_ID,
        VALID_ROUTINE_TASK_ID,
        VALID_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }

  public static ProgressDailyTaskRequest invalidRoutineTaskIdRequest() {
    return new ProgressDailyTaskRequest(
        null,
        INVALID_ROUTINE_TASK_ID,
        VALID_CURRENT_VALUE,
        VALID_TASK_DATE
    );
  }
}