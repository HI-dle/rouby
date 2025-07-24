package com.rouby.schedule.presentation.dto.request;

import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.application.dto.command.CreateScheduleCommand.RecurrenceRuleCommand;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CreateScheduleRequest (

  @NotBlank @Size(max = 500) String title,
  @Size(max = 10_000) String memo,
  @PositiveOrZero @Max(10080) Integer alarmOffsetMinutes,
  Integer routineOffsetDays,
  @NotNull LocalDateTime startAt,
  @NotNull LocalDateTime endAt,
  RecurrenceRuleRequest recurrenceRule
  ) {

  public CreateScheduleCommand toCommand() {
    return CreateScheduleCommand.builder()
        .title(title)
        .memo(memo)
        .alarmOffsetMinutes(alarmOffsetMinutes)
        .routineOffsetDays(routineOffsetDays)
        .startAt(startAt)
        .endAt(endAt)
        .recurrenceRule((recurrenceRule != null) ? recurrenceRule.toCommand() : null)
        .build();
  }

  @Builder
  public record RecurrenceRuleRequest(
      @Pattern(regexp = "^(HOURLY|DAILY|WEEKLY|MONTHLY|YEARLY)$")
      String freq,
      @Pattern(regexp = "^(MO|TU|WE|TH|FR|SA|SU)(,(MO|TU|WE|TH|FR|SA|SU))*$")
      String byDay,
      @Positive @Max(30) Integer interval,
      @Future LocalDateTime until
  ) {

    public RecurrenceRuleCommand toCommand() {
      return RecurrenceRuleCommand.builder()
          .freq(freq)
          .byDay(byDay)
          .interval(interval)
          .until(until)
          .build();
    }
  }
}
