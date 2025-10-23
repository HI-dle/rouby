package com.rouby.common.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
public class RetryAfterParser {

  private static final DateTimeFormatter HTTP_DATE_FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;

  /**
   * 응답 헤더에서 Retry-After 값을 파싱하여 대기 시간을 초 단위로 반환합니다.
   *
   * @param headers HTTP 응답 헤더
   * @return 유효한 Retry-After 값 (초 단위). 파싱에 실패하면 -1을 반환합니다.
   */
  public static long parseRetryAfterSeconds(HttpHeaders headers) {
    if (headers == null) return -1;

    String retryAfterStr = headers.getFirst(HttpHeaders.RETRY_AFTER);
    if (retryAfterStr == null) return -1;

    String trimmedValue = retryAfterStr.trim();

    // 1. 숫자 형식 (Seconds) 처리: 가장 일반적인 케이스
    try {
      long seconds = Long.parseLong(trimmedValue);
      return (seconds >= 0) ? seconds : -1;
    } catch (NumberFormatException e) {
      // 숫자가 아니면 다음 단계 (날짜 형식)로 넘어갑니다.
    }

    // 2. 날짜 형식 (HTTP-Date) 처리
    try {
      Instant now = Instant.now(); // 현재 시간 (UTC 기준)
      // HTTP 날짜 형식 파싱
      ZonedDateTime retryTime = ZonedDateTime.parse(trimmedValue, HTTP_DATE_FORMATTER);

      // 현재 시간과 재시도 시간의 차이를 초 단위로 계산
      long diff = Duration.between(now, retryTime.toInstant()).getSeconds();
      return (diff > 0) ? diff : 0; // 과거/현재인 경우 0

    } catch (Exception e) {
      // 숫자 형식도, 표준 날짜 형식도 아닌 경우
      // 로그를 남기고 -1 반환
      log.warn("Failed to parse Retry-After header: {}", trimmedValue, e);
      return -1;
    }
  }
}
