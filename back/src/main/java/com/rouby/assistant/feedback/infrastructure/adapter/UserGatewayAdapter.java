package com.rouby.assistant.feedback.infrastructure.adapter;

import com.rouby.assistant.feedback.application.dto.UserInfoForFeedback;
import com.rouby.assistant.feedback.application.port.outbound.UserGateway;
import com.rouby.user.user.application.usecase.UserUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserGatewayAdapter implements UserGateway {

  private final UserUsecase userUsecase;

  @Override
  public UserInfoForFeedback getUserInfoWithNotification(Long userId) {

    return UserInfoForFeedback.from(userUsecase.getUserInfoWithDeviceInfosBy(userId));
  }
}
