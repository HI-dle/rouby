package com.rouby.routine.daily_task.presentation.dto;

public record ProgressDailyTaskResponse(
    Long id
) {

  public static ProgressDailyTaskResponse from(Long id) {
    return new ProgressDailyTaskResponse(id);
  }
}
