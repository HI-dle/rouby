package com.rouby.user.user.application.service;

import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_USER;
import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_USER_PASSWORD;
import static com.rouby.user.user.application.exception.UserErrorCode.USER_NOT_FOUND;

import com.rouby.user.user.application.dto.command.LoginCommand;
import com.rouby.user.user.application.dto.info.LoginInfo;
import com.rouby.user.user.application.dto.info.RoubySettingInfo;
import com.rouby.user.user.application.dto.info.UserInfo;
import com.rouby.user.user.application.exception.UserException;
import com.rouby.user.user.application.service.token.TokenProvider;
import com.rouby.user.user.domain.entity.User;
import com.rouby.user.user.domain.repository.UserRepository;
import com.rouby.user.user.domain.service.UserPasswordEncoder;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserReadService {

  private final UserRepository userRepository;
  private final UserPasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;

  @Transactional(readOnly = true)
  public LoginInfo validUser(LoginCommand command) {
    User user = findByEmail(command.email());

    if (!passwordEncoder.matches(command.password(), user.getPassword())) {
      throw UserException.from(INVALID_USER_PASSWORD);
    }

    return new LoginInfo(tokenProvider.createAccessToken(
        user.getId().toString(),
        user.getRole().toString(),
        user.getEmail()));
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
  public User findByUserId(Long id) {
    return userRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() ->
        UserException.from(USER_NOT_FOUND));
  }

  @Transactional(readOnly = true)
  public RoubySettingInfo getRoubySettingInfo(Long userId) {
    return RoubySettingInfo.from(userRepository.findByIdAndDeletedAtIsNull(userId)
        .orElseThrow(() -> UserException.from(USER_NOT_FOUND)));
  }

  @Transactional(readOnly = true)
  public List<UserInfo> findUsersByBriefingTime(LocalTime briefingTime) {
    return userRepository.findActiveUsersWithBriefingNotification(briefingTime).stream()
        .map(UserInfo::of)
        .toList();
  }

}
