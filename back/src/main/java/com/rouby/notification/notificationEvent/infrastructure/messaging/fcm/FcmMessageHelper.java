package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import java.time.Duration;
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

  public Message buildCustomMessage(NotificationEvent event) {

    return Message.builder()
        .setToken(event.getDeviceTokenInfo().getDeviceToken())
        .putData("title", Objects.toString(event.getMessage().getTitle(), ""))
        .putData("body",  Objects.toString(event.getMessage().getBody(), ""))
        .putData("icon",  (iconUri.startsWith("http")
            ? iconUri
            : appUrl + (iconUri.startsWith("/") ? iconUri : ("/" + iconUri))))
        .putData("url",   Objects.toString(event.getMessage().getUrl(), ""))
        .setWebpushConfig(buildWebpushConfig())
        .build();
  }

  public Message buildBasicNotificationMessage(NotificationEvent event) {

    Notification notification = buildNotification(event);
    WebpushConfig webpushConfig = buildWebpushConfig(event);

    return Message.builder()
        .setToken(event.getDeviceTokenInfo().getDeviceToken())
        .setNotification(notification)
        .setWebpushConfig(webpushConfig)
        .build();
  }

  private WebpushConfig buildWebpushConfig() {
    return WebpushConfig.builder()
        .putHeader("TTL", TTL_SECONDS)
        .build();
  }

  private WebpushConfig buildWebpushConfig(NotificationEvent event) {
    return WebpushConfig.builder()
        .setNotification(buildWebpushNotification(event))
        .putHeader("TTL", TTL_SECONDS)
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
