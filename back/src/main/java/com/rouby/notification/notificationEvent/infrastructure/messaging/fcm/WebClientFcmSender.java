package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.sender.AsyncNotificationSender;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Exceptions;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientFcmSender implements AsyncNotificationSender {

  @Value("${fcm.projectId}")
  private String projectId;;
  private final WebClient fcmClient;
  private final GoogleAccessTokenProvider tokenProvider;
  private final FcmMessageHelper messageHelper;

  @Override
  public CompletableFuture<Boolean> sendAsync(
      NotificationEventInfo event, int ttlSec, long sentAt, boolean highPriority) {

    Map<String, Object> payload = messageHelper.buildFcmHttpV1Payload(event, ttlSec, sentAt, highPriority);

    return fcmClient.post()
        .uri("/v1/projects/{pid}/messages:send", projectId)
        .headers(h -> h.setBearerAuth(tokenProvider.fetchAccessToken()))
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(payload)
        .retrieve()
        .onStatus(HttpStatusCode::isError, resp ->
            resp.bodyToMono(String.class)
                .defaultIfEmpty("")
                .map(body -> new FcmHttpException(
                    resp.statusCode().value(),
                    "FCM HTTP error: " + resp.statusCode() + " body=" + body))
        )
        .bodyToMono(Void.class)
        .thenReturn(true)
        .timeout(Duration.ofSeconds(10))
        .doOnError(t -> {
          Throwable root = Exceptions.unwrap(t);
          log.warn("FCM 호출 실패 type={}, root={}", t.getClass().getName(), root.getClass().getName(), t);
        })
        .toFuture();
  }

  // 예시 커스텀 예외들
  public static class FcmHttpException extends RuntimeException {
    private final int status;
    public FcmHttpException(int status, String msg) { super(msg); this.status = status; }
    public int status() { return status; }
  }
}
