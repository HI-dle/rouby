package com.rouby.user.application;

import com.rouby.common.exception.CustomException;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  //Auth에서 UserService 참조하기로해서 만들어 보았는데 맞는지 잘 모르겠습니다ㅜ 피드백 주시면 수정할게용!
  @Transactional(readOnly = true)
  @Override
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> CustomException.from(UserErrorCode.INVALID_USER));
  }

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
