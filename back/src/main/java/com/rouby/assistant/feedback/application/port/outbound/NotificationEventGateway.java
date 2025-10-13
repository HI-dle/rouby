package com.rouby.assistant.feedback.application.port.outbound;

import com.rouby.assistant.feedback.application.dto.FeedbackNotiTargetUserInfo;

public interface NotificationEventGateway {

  void createFeedbackNotificationEvent(FeedbackNotiTargetUserInfo targetUserInfo, String url);
}
