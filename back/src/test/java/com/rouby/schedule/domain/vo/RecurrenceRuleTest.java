package com.rouby.schedule.domain.vo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.rouby.schedule.domain.enums.ByDay;
import com.rouby.schedule.domain.enums.Freq;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class RecurrenceRuleTest {

  @DisplayName("from(): RecurrenceRule 문자열 파싱 검증")
  @Nested
  class From {

    @DisplayName("올바른 문자열 검증 및 값 일치 검증 성공")
    @Test
    void from_success_with_checking_val() {
      // given
      String rruleStr = "FREQ=MONTHLY;INTERVAL=1;BYDAY=MO,TU;UNTIL=20251230T000000";

      // when
      RecurrenceRule rule = RecurrenceRule.from(rruleStr);

      // then
      String rRuleStr = rule.toRruleString();
      assertThat(rRuleStr).contains("FREQ=MONTHLY");
      assertThat(rRuleStr).contains("BYDAY=").contains("MO").contains("TU");
      assertThat(rRuleStr).contains("INTERVAL=1");
      assertThat(rRuleStr).contains("UNTIL=20251230T000000");
    }

    public static Stream<Arguments> validRruleStr() {
      return Stream.of(
          Arguments.of("FREQ=MONTHLY;INTERVAL=1;BYDAY=MO,TU;UNTIL=20251230T000000"),
          Arguments.of("FREQ=MONTHLY;INTERVAL=1;BYDAY=;UNTIL=20251230T000000"),
          Arguments.of("FREQ=MONTHLY;INTERVAL=1;BYDAY;UNTIL=20251230T000000"),
          Arguments.of("FREQ=MONTHLY;;INTERVAL=1"),
          Arguments.of("UNTIL=20251230T000000")
      );
    }

    @MethodSource("validRruleStr")
    @DisplayName("올바른 문자열 검증 성공")
    @ParameterizedTest(name = "{0}")
    void from_success(String rruleStr) {

      // when, then
      assertThatCode(() -> RecurrenceRule.from(rruleStr)).doesNotThrowAnyException();
    }

    public static Stream<Arguments> rruleStrWithInvalidKey() {
      return Stream.of(
          Arguments.of("FREQ=MONTHLY;INVALIDKEY=123"),
          Arguments.of("FREQ=MONTHLY;=123")
      );
    }
    @MethodSource("rruleStrWithInvalidKey")
    @DisplayName("잘못된 룰 키 값으로 인한 예외 발생")
    @ParameterizedTest(name = "{0}")
    void from_invalid_key_fail(String rruleStr) {

      // given, expect
      assertThatThrownBy(() -> RecurrenceRule.from(rruleStr))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("RRule 타입");
    }

    public static Stream<Arguments> rruleStrWithInvalidUntil() {
      return Stream.of(
          Arguments.of("UNTIL=20251230"),
          Arguments.of("UNTIL=20251230T"),
          Arguments.of("UNTIL=20251230T00"),
          Arguments.of("UNTIL=invalid-date")
      );
    }
    @MethodSource("rruleStrWithInvalidUntil")
    @DisplayName("잘못된 until 포맷으로 인한 예외 발생")
    @ParameterizedTest(name = "{0}")
    void fromWithInvalidUntilData(String rruleStr) {

      // given, expect
      assertThatThrownBy(() -> RecurrenceRule.from(rruleStr))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("RecurrenceRule 파싱 중 오류");
    }

    @DisplayName("중복된 룰 키 값으로 인한 예외 발생")
    @Test
    void fromWithDuplicatedKeyData() {
      // given
      String rruleStr = "FREQ=MONTHLY;;FREQ=DAILY";

      // expect
      assertThatThrownBy(() -> RecurrenceRule.from(rruleStr))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("RecurrenceRule 파싱 중 오류");
    }

    @DisplayName("잘못된 인터벌 값으로 인한 예외 발생")
    @Test
    void fromWithInvalidIntervalData() {
      // given
      String rruleStr = "FREQ=MONTHLY;INTERVAL=30";

      // expect
      assertThatThrownBy(() -> RecurrenceRule.from(rruleStr))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("부적절한 인터벌 값");
    }
  }



  @DisplayName("toString(): RecurrenceRule 문자열 변환 검증")
  @Nested
  class ToString {

    @DisplayName("올바른 RRule 정보 검증 성공")
    @Test
    void toStringSuccess() {
      // given
      RecurrenceRule rule = RecurrenceRule.builder()
          .freq(Freq.MONTHLY)
          .interval(2)
          .byDay(Set.of(ByDay.MO, ByDay.FR))
          .until(LocalDateTime.of(2025, 12, 30, 0, 0, 0, 0))
          .build();

      // when
      String result = rule.toRruleString();

      // then
      assertThat(result).contains("FREQ=MONTHLY");
      assertThat(result).contains("INTERVAL=2");
      assertThat(result).contains("BYDAY=").contains("MO").contains("FR");
      assertThat(result).contains("UNTIL=20251230T000000");
    }

//    @DisplayName("UTC 존을 가진 RRule 정보 검증 성공")
//    @Test
//    void toStringSuccess_with_UTC_zone_util() {
//      // given
//      RecurrenceRule rule = RecurrenceRule.from("UNTIL=20251230T000000Z");
//
//      // when
//      String result = rule.toRruleString();
//
//      // then
//      assertThat(result).contains("UNTIL=20251230T000000Z");
//    }
  }
}