package com.rouby.schedule.application.dto.info;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record SchedulesSummaryInfo(
    List<ScheduleSummaryInfo> schedules
) {
  private static final ObjectMapper objectMapper = new ObjectMapper();

  public static SchedulesSummaryInfo of(List<ScheduleWithOverrides> schedules) {

    return SchedulesSummaryInfo.builder()
        .schedules(safeList(schedules).stream()
            .map(SchedulesSummaryInfo::mapToScheduleSummary)
            .toList())
        .build();
  }

  public static <T> List<T> safeList(List<T> list) {
    return (list == null) ? List.of() : list;
  }

  private static ScheduleSummaryInfo mapToScheduleSummary(ScheduleWithOverrides schedule) {
    return ScheduleSummaryInfo.builder()
        .title(schedule.title())
        .memo(schedule.memo())
        .startAt(schedule.startAt())
        .endAt(schedule.endAt())
        .recurrenceRule(
            schedule.recurrenceRule() != null ? schedule.recurrenceRule().toRruleString() : "NONE")
        .overrides(
            safeList(schedule.scheduleOverrides()).stream()
                .map(SchedulesSummaryInfo::mapToScheduleOverrideSummary)
                .toList()
        )
        .build();
  }

  public String toJson() {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize SchedulesSummaryInfo to JSON", e);
    }
  }
  @Builder
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public record ScheduleSummaryInfo(
      String title,
      String memo,
      @JsonSerialize(using = LocalDateTimeSerializer.class)
      LocalDateTime startAt,
      @JsonSerialize(using = LocalDateTimeSerializer.class)
      LocalDateTime endAt,
      String recurrenceRule,
      List<ScheduleOverrideSummary> overrides
  ) {

  }

  @Builder
  public record ScheduleOverrideSummary(
      String title,
      String memo,
      @JsonSerialize(using = LocalDateTimeSerializer.class)
      LocalDateTime startAt,
      @JsonSerialize(using = LocalDateTimeSerializer.class)
      LocalDateTime endAt,
      String overrideType,
      @JsonSerialize(using = LocalDateSerializer.class)
      LocalDate overrideDate
  ) {

  }

  private static ScheduleOverrideSummary mapToScheduleOverrideSummary(
      ScheduleWithOverrides.ScheduleOverride override) {
    return ScheduleOverrideSummary.builder()
        .title(override.title())
        .memo(override.memo())
        .startAt(override.startAt())
        .endAt(override.endAt())
        .overrideType(override.overrideType().toString())
        .overrideDate(override.overrideDate())
        .build();
  }
}
