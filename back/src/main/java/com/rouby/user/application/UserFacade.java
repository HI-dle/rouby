package com.rouby.user.application;

import com.rouby.user.application.dto.command.ResetPasswordCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserService userService;

  public void resetPassword(Long userId, ResetPasswordCommand command) {
    userService.resetPassword(userId, command);
  }

}
