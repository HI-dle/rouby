package com.rouby.schedule.domain.vo;


import com.rouby.schedule.domain.entity.ByDaySetConverter;
import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import com.rouby.schedule.domain.support.UntilDateTimeFormatter;
import io.jsonwebtoken.lang.Assert;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecurrenceRule implements Serializable {

  private Freq freq;
  @Convert(converter = ByDaySetConverter.class)
  private Set<ByDay> byDay;
  private Integer interval;
  private LocalDateTime until;

  @Builder
  private RecurrenceRule(Freq freq, Set<ByDay> byDay, Integer interval, LocalDateTime until) {
    this.freq = freq;
    this.byDay = byDay;
    this.interval = interval;
    this.until = until;

    validate();
  }

  public String toRruleString() {

    String separator = ";";
    String equal = "=";
    StringBuilder sb = new StringBuilder();

    if (freq != null) {
      sb.append(RuleType.FREQ).append(equal).append(freq).append(separator);
    }
    if (byDay != null && !byDay.isEmpty()) {
      String byDayStr = String.join(",", byDay.stream().map(Enum::name).toList());
      sb.append(RuleType.BYDAY).append(equal).append(byDayStr).append(separator);
    }
    if (interval != null) {
       sb.append(RuleType.INTERVAL).append(equal).append(interval).append(separator);
    }
    if (until != null) {
      String formattedUntilStr = UntilDateTimeFormatter.format(until);
      sb.append(RuleType.UNTIL).append(equal).append(formattedUntilStr).append(separator);
    }
    return sb.toString();
  }

  public static RecurrenceRule from(@NotNull String str) {

    Assert.notNull(str, "RecurrenceRule 문자열은 null일 수 없습니다.");

    RecurrenceRule rRule = new RecurrenceRule();
    String[] split = str.split(";");
    Arrays.stream(split)
        .forEach(item -> {

          String[] keyVal = item.split("=");
          if (keyVal.length <= 1) return;
          if (keyVal.length > 2) {
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

  private void validate() {
    if (this.interval == null || this.freq == null) return;
    this.freq.validateInterval(this.interval);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  enum RuleType {
    FREQ(str -> Freq.valueOf(str),
        (rrule, freq) -> {
      if (rrule.freq != null) throw new IllegalArgumentException("FREQ 중복 설정");
      rrule.freq = (Freq) freq;
    }),
    BYDAY(str -> Arrays.stream(str.split(","))
        .map(ByDay::valueOf)
        .collect(Collectors.toSet()),
        (rrule, byDay) -> {
      if (rrule.byDay != null) throw new IllegalArgumentException("BYDAY 중복 설정");
      rrule.byDay = (Set<ByDay>) byDay;
    }),
    INTERVAL(str -> Integer.valueOf(str),
        (rrule, interval) -> {
      if (rrule.interval != null) throw new IllegalArgumentException("INTERVAL 중복 설정");
      rrule.interval = (Integer) interval;
    }),
    UNTIL(str -> UntilDateTimeFormatter.parse(str),
        (rrule, until) -> {
      if (rrule.until != null) throw new IllegalArgumentException("UNTIL 중복 설정");
      rrule.until = (LocalDateTime) until;
    }),
    ;

    private final Function<String, Object> stringConverter;
    private final BiConsumer<RecurrenceRule, Object> ruleSetter;

    public static RuleType parse(String str) {
      if (str == null || str.isBlank()) throw new IllegalArgumentException("RRule 타입 값이 빈 값일 수 없습니다.");

      try {
        return RuleType.valueOf(str.trim().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("잘못된 RRule 타입이 확인되었습니다.: " + str, e);
      }
    }

    public Object convert(String trim) {
      return this.stringConverter.apply(trim);
    }

    public void setRule(RecurrenceRule rrule, Object val) {
      this.ruleSetter.accept(rrule, val);
    }
  }
}