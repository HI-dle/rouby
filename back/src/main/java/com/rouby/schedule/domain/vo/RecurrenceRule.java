package com.rouby.schedule.domain.vo;


import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import java.time.LocalDate;
import java.time.LocalDateTime;
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

@NoArgsConstructor
public class RecurrenceRule {

  private Freq freq;
  private Set<ByDay> byDay;
  private Integer interval;
  private LocalDateTime until;


  @Builder
  public RecurrenceRule(Freq freq, Set<ByDay> byDay, Integer interval, LocalDateTime until) {
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

    String byDayStr = String.join(",", byDay.stream().map(Enum::name).toList());
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX");
    String untilStr = formatter.format(until);

    StringBuilder sb = new StringBuilder();
    sb.append("FREQ=").append(freq).append(separator)
        .append("BYDAY=").append(byDayStr).append(separator)
        .append("INTERVAL=").append(interval).append(separator)
        .append("UNTIL=").append(untilStr).append(separator);

    return sb.toString();
  }

  public static RecurrenceRule from(String str) {

    RecurrenceRule rRule = new RecurrenceRule();

    String[] splited = str.split(";");
    Arrays.stream(splited)
        .forEach(item -> {
          String[] keyVal = item.split("=");

          RuleType ruleType = RuleType.valueOf(keyVal[0].trim().toUpperCase());
          Object val = ruleType.convert(keyVal[1].trim());
          ruleType.setRule(rRule, val);
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
    UNTIL(str -> LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX")),
        (rrule, until) -> rrule.until = (LocalDateTime) until),
    ;

    private final Function<String, Object> stringConverter;
    private final BiConsumer<RecurrenceRule, Object> ruleSetter;

    public Object convert(String trim) {
      return this.stringConverter.apply(trim);
    }

    public void setRule(RecurrenceRule rrule, Object val) {
      this.ruleSetter.accept(rrule, val);
    }
  }
}