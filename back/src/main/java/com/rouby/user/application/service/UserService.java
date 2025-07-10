package com.rouby.user.application.service;

import com.rouby.common.exception.CustomException;
import com.rouby.user.application.UserErrorCode;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Transactional
  public void resetPassword(Long userId, ResetPasswordCommand command) {
    User user = userRepository.findById(userId).orElseThrow(() ->
        new CustomException(UserErrorCode.USER_NOT_FOUND));

    if (!passwordEncoder.matches(command.currentPassword(), user.getPassword())) {
      throw new CustomException(UserErrorCode.INVALID_PASSWORD);
    }

    user.updatePassword(passwordEncoder.encode(command.newPassword()));
  }

}