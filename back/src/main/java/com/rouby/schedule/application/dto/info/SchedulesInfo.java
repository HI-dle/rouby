package com.rouby.schedule.application.dto.info;

import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import com.rouby.schedule.presentation.dto.response.SchedulesResponse;
import com.rouby.schedule.presentation.dto.response.SchedulesResponse.RecurrenceRuleResponse;
import com.rouby.schedule.presentation.dto.response.SchedulesResponse.ScheduleOverrideResponse;
import com.rouby.schedule.presentation.dto.response.SchedulesResponse.ScheduleResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;

@Builder
public record SchedulesInfo(
    List<ScheduleInfo> schedules
) {

  public static SchedulesInfo of(List<ScheduleWithOverrides> schedulesByCriteria) {

    return SchedulesInfo.builder()
        .schedules(schedulesByCriteria.stream()
            .map(schedule ->
                ScheduleInfo.builder()
                    .id(schedule.id())
                    .title(schedule.title())
                    .memo(schedule.memo())
                    .startAt(schedule.startAt())
                    .endAt(schedule.endAt())
                    .routineActivateDate(schedule.routineActivateDate())
                    .alarmOffsetMinutes(schedule.alarmOffsetType().getMinutes())
                    .recurrenceRule(RecurrenceRuleInfo.builder()
                        .freq(schedule.recurrenceRule().getFreq().toString())
                        .interval(schedule.recurrenceRule().getInterval())
                        .until(schedule.recurrenceRule().getUntil())
                        .byDay(schedule.recurrenceRule().getByDay().stream()
                            .map(Enum::toString)
                            .collect(Collectors.toSet()))
                            .build())
                    .scheduleOverrides(
                        schedule.scheduleOverrides().stream()
                            .map(override->
                                ScheduleOverrideInfo.builder()
                                    .id(override.id())
                                    .userId(override.userId())
                                    .startAt(override.startAt())
                                    .endAt(override.endAt())
                                    .alarmOffsetMinutes(override.alarmOffsetType().getMinutes())
                                    .overrideDate(override.overrideDate())
                                    .overrideType(override.overrideType().toString())
                                    .build()
                            )
                            .toList()
                    )
                    .build()
            )
            .toList())
        .build();
  }

  public SchedulesResponse toResponse() {
    return SchedulesResponse.builder()
        .schedules(this.schedules.stream()
            .map(info ->
                ScheduleResponse.builder()
                    .id(info.id)
                    .title(info.title)
                    .memo(info.memo)
                    .alarmOffsetMinutes(info.alarmOffsetMinutes)
                    .startAt(info.startAt)
                    .endAt(info.endAt)
                    .routineActivateDate(info.routineActivateDate())
                    .recurrenceRule(
                        RecurrenceRuleResponse.builder()
                            .freq(info.recurrenceRule.freq)
                            .until(info.recurrenceRule.until)
                            .interval(info.recurrenceRule.interval)
                            .byDay(info.recurrenceRule.byDay)
                            .build()
                    )
                    .scheduleOverrides(
                        info.scheduleOverrides().stream()
                            .map(override->
                                ScheduleOverrideResponse.builder()
                                    .id(override.id())
                                    .userId(override.userId())
                                    .startAt(override.startAt())
                                    .endAt(override.endAt())
                                    .alarmOffsetMinutes(override.alarmOffsetMinutes())
                                    .overrideDate(override.overrideDate())
                                    .overrideType(override.overrideType())
                                    .build()
                            )
                            .toList()
                    )
                    .build()
            )
            .toList())
        .build();
  }

  @Builder
  record ScheduleInfo(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      LocalDate routineActivateDate,
      Integer alarmOffsetMinutes,
      RecurrenceRuleInfo recurrenceRule,
      List<ScheduleOverrideInfo> scheduleOverrides
  ) {
  }

  @Builder
  record RecurrenceRuleInfo(
      String freq,
      Set<String> byDay,
      Integer interval,
      LocalDateTime until
  ) {
  }

  @Builder
  record ScheduleOverrideInfo(
      Long id,
      Long userId,
      String title,
      String memo,
      LocalDateTime startAt,
      LocalDateTime endAt,
      Integer alarmOffsetMinutes,
      String overrideType,
      LocalDate overrideDate
  ) {
  }
}
