package com.rouby.user.application;

import com.rouby.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.application.dto.response.ValidateTokenInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserService userService;

  public void findPassword(FindPasswordCommand command) {
    userService.findPassword(command);
  }

  public void resetPasswordByToken(ResetPasswordCommand command) {
    userService.resetPasswordByToken(command);
  }
  public ValidateTokenInfo validatePasswordToken(String token) {
    return userService.validatePasswordToken(token);
  }

}
