package com.rouby.user.application.service;

import static com.rouby.user.application.exception.UserErrorCode.INVALID_USER;
import static com.rouby.user.application.exception.UserErrorCode.INVALID_USER_PASSWORD;
import static com.rouby.user.application.exception.UserErrorCode.USER_NOT_FOUND;

import com.rouby.user.application.dto.command.LoginCommand;
import com.rouby.user.application.dto.info.RoubySettingInfo;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.domain.service.UserPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserReadService {

  private final UserRepository userRepository;
  private final UserPasswordEncoder passwordEncoder;

  @Transactional(readOnly = true)
  public User validUser(LoginCommand command) {
    User user = findByEmail(command.email());

    if (!passwordEncoder.matches(command.password(), user.getPassword())) {
      throw UserException.from(INVALID_USER_PASSWORD);
    }

    return user;
  }

  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> UserException.from(INVALID_USER));
  }

  public boolean alreadyExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  @Transactional(readOnly = true)
  public RoubySettingInfo getRoubySettingInfo(Long userId) {
    return RoubySettingInfo.from(userRepository.findById(userId)
        .orElseThrow(() -> UserException.from(USER_NOT_FOUND)));
  }
}
