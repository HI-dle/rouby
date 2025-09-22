package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.domain.sender.AsyncNotificationSender;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@RequiredArgsConstructor
public class WebClientFcmSender implements AsyncNotificationSender {

  private final String projectId = "rouby-b3e76";
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
        .exchangeToMono(resp -> Mono.just(resp.statusCode().is2xxSuccessful()))
        .toFuture();
  }


}
