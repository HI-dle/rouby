package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FcmMessageHelper {

  private static final String TTL_SECONDS = String.valueOf(Duration.ofHours(1).toSeconds());

  private final String appUrl;
  private final String iconUri;

  public FcmMessageHelper(
      @Value("${app.front-url}") String appUrl,
      @Value("${app.icon-uri}") String iconUri
  ) {
    this.appUrl = appUrl;
    this.iconUri = iconUri;
  }

  public Map<String, Object> buildFcmHttpV1Payload(NotificationEventInfo event,
      int ttlSec, long sentAtMs, boolean highPriority) {

    // 공통 data
    Map<String, String> data = new HashMap<>();
    data.put("eventId", String.valueOf(event.id()));
    data.put("dueAt", String.valueOf(event.dueAt().toEpochMilli()));
    data.put("sentAt", String.valueOf(sentAtMs));
    data.put("priority", highPriority ? "high" : "normal");

    // WebPush 설정 (브라우저용)
    Map<String, Object> webpushHeaders = new HashMap<>();
    webpushHeaders.put("TTL", String.valueOf(ttlSec));
    webpushHeaders.put("Urgency", highPriority ? "high" : "normal");  // very-low/low/normal/high
    // webpushHeaders.put("Topic", "event-" + ev.id()); // (선택) 중복 억제

    Map<String, Object> webpushNotif = Map.of(
        "title", event.message().getTitle(),
        "body", event.message().getBody(),
        "tag", "event-" + event.id(), // 브라우저 측 대체 키
        "icon",  (iconUri.startsWith("http")
            ? iconUri
            : appUrl + (iconUri.startsWith("/") ? iconUri : ("/" + iconUri)))
    );

    Map<String, Object> webpush = new HashMap<>();
    webpush.put("headers", webpushHeaders);
    webpush.put("notification", webpushNotif);
    webpush.put("fcm_options", Map.of("link", event.message().getUrl()));

    // Android 설정
    Map<String, Object> android = new HashMap<>();
    android.put("priority", highPriority ? "HIGH" : "NORMAL"); // HIGH/NORMAL
    android.put("ttl", ttlSec + "s");                          // "60s" 형식
    // android.put("collapse_key", "event-" + ev.id());        // (선택) 안드 대체 키

    // APNs(iOS) 설정 (웹푸시가 아닌 네이티브 iOS 앱 쓰는 경우에만)
    long expirationEpochSec = (sentAtMs + ttlSec * 1000L) / 1000L;
    Map<String, String> apnsHeaders = new HashMap<>();
    apnsHeaders.put("apns-priority", highPriority ? "10" : "5"); // 10=즉시, 5=배경
    apnsHeaders.put("apns-expiration", String.valueOf(expirationEpochSec));
    Map<String, Object> apns = Map.of("headers", apnsHeaders);

    // 최종 message
    Map<String, Object> message = new HashMap<>();
    message.put("token", event.deviceTokenInfo().getDeviceToken());
    message.put("data", data);
    message.put("webpush", webpush);   // 웹 대상일 때 유효
    message.put("android", android);   // 안드로이드 앱 대상일 때 유효
    message.put("apns", apns);         // iOS 앱 대상일 때 유효

    return Map.of("message", message);
  }

  public Message buildCustomMessage(NotificationEventInfo event, int ttl, long sentAt, boolean highPriority) {

    return Message.builder()
        .setToken(event.deviceTokenInfo().getDeviceToken())
        .putData("eventId", String.valueOf(event.id()))
        .putData("dueAt", String.valueOf(event.dueAt().toEpochMilli()))
        .putData("sentAt", String.valueOf(sentAt))
        .putData("title", Objects.toString(event.message().getTitle(), ""))
        .putData("body",  Objects.toString(event.message().getBody(), ""))
        .putData("icon",  (iconUri.startsWith("http")
            ? iconUri
            : appUrl + (iconUri.startsWith("/") ? iconUri : ("/" + iconUri))))
        .putData("url",   Objects.toString(event.message().getUrl(), ""))
        .setWebpushConfig(buildWebpushConfig(ttl))
        .build();
  }

  public Message buildBasicNotificationMessage(NotificationEvent event, int ttl) {

    Notification notification = buildNotification(event);
    WebpushConfig webpushConfig = buildWebpushConfig(event, ttl);

    return Message.builder()
        .setToken(event.getDeviceTokenInfo().getDeviceToken())
        .setNotification(notification)
        .setWebpushConfig(webpushConfig)
        .build();
  }

  private WebpushConfig buildWebpushConfig(int ttl) {
    return WebpushConfig.builder()
        .putHeader("TTL", String.valueOf(ttl))
        .build();
  }

  private WebpushConfig buildWebpushConfig(NotificationEvent event, int ttl) {
    return WebpushConfig.builder()
        .setNotification(buildWebpushNotification(event))
        .putHeader("TTL", String.valueOf(ttl))
        .setFcmOptions(buildFcmOptions(event))
        .build();
  }

  private Notification buildNotification(NotificationEvent event) {

    return Notification.builder()
        .setTitle(event.getMessage().getTitle())
        .setBody(event.getMessage().getBody())
        .build();
  }

  private WebpushFcmOptions buildFcmOptions(NotificationEvent event) {

    return WebpushFcmOptions.builder()
        .setLink(event.getMessage().getUrl())
        .build();
  }

  private WebpushNotification buildWebpushNotification(NotificationEvent event) {

    return WebpushNotification.builder()
        .setTitle(event.getMessage().getTitle())
        .setBody(event.getMessage().getBody())
        .setIcon(appUrl + iconUri)
        .putCustomData("url", event.getMessage().getUrl())
        .build();
  }
}
