package com.rouby.user.application;

import com.rouby.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserService userService;

  public void findPassword(FindPasswordCommand command) {
    userService.findPassword(command);
  }

  public void resetPasswordByToken(String token, ResetPasswordCommand command) {
    userService.resetPasswordByToken(token, command);
  }
}
