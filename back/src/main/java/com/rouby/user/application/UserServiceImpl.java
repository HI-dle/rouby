package com.rouby.user.application;

import com.rouby.common.exception.CustomException;
import com.rouby.common.props.URIProperty;
import com.rouby.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 08.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final URIProperty uriProperty;

  //Auth에서 UserService 참조하기로해서 만들어 보았는데 맞는지 잘 모르겠습니다ㅜ 피드백 주시면 수정할게용!
  @Transactional(readOnly = true)
  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> CustomException.from(UserErrorCode.INVALID_USER));
  }

  @Transactional
  public void resetPasswordByToken(ResetPasswordCommand command) {
    Long userId = userRepository.getUserIdByToken(command.token())
        .orElseThrow(() -> CustomException.from(UserErrorCode.PASSWORD_TOKEN_EXPIRED));

    User user = userRepository.findById(userId)
        .orElseThrow(() -> CustomException.from(UserErrorCode.USER_NOT_FOUND));

    user.updatePassword(passwordEncoder.encode(command.newPassword()));

    userRepository.delete(command.token());
  }

  public void findPassword(FindPasswordCommand command) {
    User user = userRepository.findByEmail(command.email())
        .orElseThrow(() -> CustomException.from(UserErrorCode.EMAIL_NOT_FOUND));

    String token = UUID.randomUUID().toString();

    userRepository.storePasswordResetToken(token, user.getId(), Duration.ofMinutes(15));

    String resetPasswordLink = uriProperty.generateResetPasswordLink(token);

    //emailService.sendResetPasswordEmail(user.getEmail(), uriProperty.getResetPasswordLink());
  }

}
