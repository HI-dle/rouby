package com.rouby.user.application.service;

import static com.rouby.user.application.exception.UserErrorCode.INVALID_USER;

import com.rouby.common.exception.CustomException;
import com.rouby.common.props.URIProperty;
import com.rouby.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.application.dto.response.ValidateTokenInfo;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.presentation.dto.response.ValidateTokenResponse;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserReadService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final URIProperty uriProperty;

  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> CustomException.from(INVALID_USER));
  }

  public boolean alreadyExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Transactional
  public void resetPasswordByToken(ResetPasswordCommand command) {
    User user = userRepository.findById(command.userId())
        .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));

    user.updatePassword(passwordEncoder.encode(command.newPassword()));
  }

  public void findPassword(FindPasswordCommand command) {
    User user = userRepository.findByEmail(command.email())
        .orElseThrow(() -> CustomException.from(UserErrorCode.EMAIL_NOT_FOUND));

    String token = UUID.randomUUID().toString();

    userRepository.storePasswordResetToken(token, user.getId(), Duration.ofMinutes(15));

    String resetPasswordLink = uriProperty.generateResetPasswordLink(token);

    //emailService.sendResetPasswordEmail(user.getEmail(), uriProperty.getResetPasswordLink());
  }

  public ValidateTokenInfo validatePasswordToken(String token) {
    Long userId = userRepository.getUserIdByToken(token)
        .orElseThrow(() -> CustomException.from(UserErrorCode.PASSWORD_TOKEN_EXPIRED));

    userRepository.delete(token);

    return ValidateTokenInfo.builder().userId(userId).build();
  }
}
