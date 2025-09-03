package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import com.rouby.notification.notificationEvent.domain.entity.NotificationEvent;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FcmMessageHelper {

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
        .putData("title", event.getMessage().getTitle())
        .putData("body",  event.getMessage().getBody())
        .putData("icon",  appUrl + iconUri)
        .putData("url", event.getMessage().getUrl())
        .setWebpushConfig(
            WebpushConfig.builder()
            .putHeader("TTL", String.valueOf(Duration.ofHours(1).toSeconds()))
            .build())
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

  private WebpushConfig buildWebpushConfig(NotificationEvent event) {
    return WebpushConfig.builder()
        .setNotification(buildWebpushNotification(event))
        .putHeader("TTL", String.valueOf(Duration.ofHours(1).toSeconds()))
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
