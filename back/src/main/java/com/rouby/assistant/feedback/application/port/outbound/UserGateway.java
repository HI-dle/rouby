package com.rouby.assistant.feedback.application.port.outbound;

import com.rouby.assistant.feedback.application.dto.UserInfoForFeedback;

public interface UserGateway {

  UserInfoForFeedback getUserInfoWithNotification(Long aLong);
}
