package com.rouby.notification.notificationEvent.infrastructure.messaging.fcm;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.rouby.common.exception.type.ApiErrorCode;
import com.rouby.notification.notificationEvent.domain.sender.NotificationSender;
import com.rouby.notification.notificationEvent.domain.info.NotificationEventInfo;
import com.rouby.notification.notificationEvent.infrastructure.exception.NotificationEventInfraException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class FcmSender implements NotificationSender {

  private final FirebaseMessaging firebaseMessaging;
  private final FcmMessageHelper fcmMessageHelper;

  @Override
  public boolean send(NotificationEventInfo event, int ttl, long sentAt, boolean highPriority) {

    Message message = fcmMessageHelper.buildCustomMessage(event, ttl, sentAt, highPriority);
    try {
      firebaseMessaging.send(message);
      return true;
    } catch (FirebaseMessagingException e) {
      log.error("firebase 알림 전송 실패: {}", e.getMessage(), e);
      throw NotificationEventInfraException.from(ApiErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}
