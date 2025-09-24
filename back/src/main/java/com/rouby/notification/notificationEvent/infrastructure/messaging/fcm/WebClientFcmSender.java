package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.sender.AsyncNotificationSender;
import com.rouby.notification.notificationEvent.infrastructure.exception.NotificationEventFcmException;
import com.rouby.notification.notificationEvent.infrastructure.exception.NotificationEventFcmRetryableException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
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
        .onStatus(HttpStatusCode::isError, resp -> resp.bodyToMono(String.class)
              .defaultIfEmpty("")
              .map(body -> {

                if (resp.statusCode().is4xxClientError() && resp.statusCode().value() != 429) {
                  return new NotificationEventFcmException(
                      (HttpStatus) resp.statusCode(),
                      "FCM WebClient error: " + resp.statusCode() + " body=" + body);
                }

                String retryAfter = resp.headers().asHttpHeaders().getFirst("Retry-After");
                return new NotificationEventFcmRetryableException(
                    (HttpStatus) resp.statusCode(),
                    "FCM WebClient error: " + resp.statusCode() + " body=" + body,
                    retryAfter);
              })
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
}
