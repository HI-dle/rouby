package com.rouby.schedule.presentation.dto.request;

import com.rouby.schedule.application.dto.command.CreateScheduleCommand;
import com.rouby.schedule.application.dto.command.CreateScheduleCommand.RecurrenceRuleCommand;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Builder
public record CreateScheduleRequest (

  @NotBlank @Size(max = 500) String title,
  @Size(max = 10_000) String memo,
  @PositiveOrZero @Max(10080) Integer alarmOffsetMinutes,
  @FutureOrPresent LocalDate routineActivateDate,
  @NotNull LocalDate startDate,
  @NotNull LocalTime startTime,
  @NotNull LocalDate endDate,
  @NotNull LocalTime endTime,
  @Validated RecurrenceRuleRequest recurrenceRule
  ) {

  public CreateScheduleCommand toCommand() {
    return CreateScheduleCommand.builder()
        .title(title)
        .memo(memo)
        .alarmOffsetMinutes(alarmOffsetMinutes)
        .routineActivateDate(routineActivateDate)
        .startDate(startDate)
        .startTime(startTime)
        .endDate(endDate)
        .endTime(endTime)
        .recurrenceRule(recurrenceRule.toCommand())
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
