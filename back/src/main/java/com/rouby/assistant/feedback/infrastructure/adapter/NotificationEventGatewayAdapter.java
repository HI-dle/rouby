package com.rouby.assistant.feedback.infrastructure.adapter;

import com.rouby.assistant.feedback.application.dto.FeedbackNotiTargetUserInfo;
import com.rouby.assistant.feedback.application.port.outbound.NotificationEventGateway;
import com.rouby.notification.notificationEvent.application.dto.CreateNotificationEventCommand;
import com.rouby.notification.notificationEvent.application.usecase.CreateNotificationEventUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class NotificationEventGatewayAdapter implements NotificationEventGateway {

  private final CreateNotificationEventUsecase createNotificationEventUsecase;

  @Override
  public void createFeedbackNotificationEvent(
      FeedbackNotiTargetUserInfo targetUserInfo, String url) {

    createNotificationEventUsecase.create(
        CreateNotificationEventCommand.from(targetUserInfo, url), targetUserInfo.notificationEnabled());
  }
}
