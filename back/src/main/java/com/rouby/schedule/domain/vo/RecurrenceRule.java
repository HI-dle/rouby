package com.rouby.schedule.domain.vo;


import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import jakarta.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecurrenceRule {

  private Freq freq;
  private Set<ByDay> byDay;
  private Integer interval;
  private ZonedDateTime until;


  @Builder
  private RecurrenceRule(Freq freq, Set<ByDay> byDay, Integer interval, ZonedDateTime until) {
    this.freq = freq;
    this.byDay = byDay;
    this.interval = interval;
    this.until = until;
    validate();
  }

  public void validate() {
    this.freq.validateInterval(this.interval);
  }

  @Override
  public String toString() {

    String separator = ";";
    String equal = "=";

    String byDayStr = byDay != null ? String.join(",", byDay.stream().map(Enum::name).toList()) : "";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");
    String untilStr = until != null ? formatter.format(until): "";

    StringBuilder sb = new StringBuilder();
    sb.append(RuleType.FREQ).append(equal).append(freq).append(separator)
        .append(RuleType.BYDAY).append(equal).append(byDayStr).append(separator)
        .append(RuleType.INTERVAL).append(equal).append(interval).append(separator)
        .append(RuleType.UNTIL).append(equal).append(untilStr).append(separator);

    return sb.toString();
  }

  public static RecurrenceRule from(@NotNull String str) {

    RecurrenceRule rRule = new RecurrenceRule();
    String[] split = str.split(";");
    Arrays.stream(split)
        .forEach(item -> {

          String[] keyVal = item.split("=");
          if (keyVal.length != 2) {
            throw new IllegalArgumentException("데이터에 잘못된 RecurrenceRule 형식이 존재합니다.: " + item);
          }

          RuleType ruleType = RuleType.parse(keyVal[0]);
          try {
            Object val = ruleType.convert(keyVal[1].trim());
            ruleType.setRule(rRule, val);
          } catch (Exception e) {
            throw new IllegalArgumentException("RecurrenceRule 파싱 중 오류가 발생하였습니다: " + item, e);
          }
        });

    rRule.validate();
    return rRule;
  }


  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  enum RuleType {
    FREQ(str -> Freq.valueOf(str),
        (rrule, freq) -> rrule.freq = (Freq) freq),
    BYDAY(str -> Arrays.stream(str.split(","))
        .map(ByDay::valueOf)
        .collect(Collectors.toSet()),
        (rrule, byDay) -> rrule.byDay = (Set<ByDay>) byDay),
    INTERVAL(str -> Integer.valueOf(str),
        (rrule, interval) -> rrule.interval = (Integer) interval),
    UNTIL(str -> ZonedDateTime.parse(str, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX")),
        (rrule, until) -> rrule.until = (ZonedDateTime) until),
    ;

    private final Function<String, Object> stringConverter;
    private final BiConsumer<RecurrenceRule, Object> ruleSetter;

    public static RuleType parse(String str) {
      if (str == null || str.isBlank()) return null;

      RuleType ruleType;
      try {
        ruleType = RuleType.valueOf(str.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("잘못된 RRule 타입이 확인되었습니다.: " + str, e);
      }
      return ruleType;
    }

    public Object convert(String trim) {
      return this.stringConverter.apply(trim);
    }

    public void setRule(RecurrenceRule rrule, Object val) {
      this.ruleSetter.accept(rrule, val);
    }
  }
}