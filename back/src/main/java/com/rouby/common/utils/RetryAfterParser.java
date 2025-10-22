package com.rouby.common.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

@Slf4j
public class RetryAfterParser {

  private static final DateTimeFormatter HTTP_DATE_FORMATTER = DateTimeFormatter
      .ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
      .withZone(ZoneOffset.UTC);

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
      return Long.parseLong(trimmedValue);
    } catch (NumberFormatException e) {
      // 숫자가 아니면 다음 단계 (날짜 형식)로 넘어갑니다.
    }

    // 2. 날짜 형식 (HTTP-Date) 처리
    try {
      Instant now = Instant.now(); // 현재 시간 (UTC 기준)

      // HTTP 날짜 형식 파싱 (ZonedDateTime으로 파싱해야 GMT를 인식)
      ZonedDateTime retryTime = ZonedDateTime.parse(trimmedValue, HTTP_DATE_FORMATTER);

      // 현재 시간과 재시도 시간의 차이를 초 단위로 계산
      // (재시도 시간이 현재 시간보다 이전이면 0 또는 -1을 반환하도록 처리 가능)
      if (retryTime.toInstant().isAfter(now)) {
        return retryTime.toInstant().getEpochSecond() - now.getEpochSecond();
      } else {
        return 0; // 이미 재시도 시간이 지났거나 현재와 동일한 경우
      }

    } catch (Exception e) {
      // 숫자 형식도, 표준 날짜 형식도 아닌 경우
      // 로그를 남기고 -1 반환 (무시하던 로직을 유지)
      log.warn("Failed to parse Retry-After header: {}", trimmedValue, e);
      return -1;
    }
  }
}
