package com.rouby.user.application;

import com.rouby.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.application.dto.response.ValidateTokenInfo;
import com.rouby.user.domain.entity.User;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 08.
 */

public interface UserService {

  User findByEmail(String email);

  void resetPasswordByToken(ResetPasswordCommand command);

  void findPassword(FindPasswordCommand command);

  ValidateTokenInfo validatePasswordToken(String token);
}
