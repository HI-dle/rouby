package com.rouby.auth.application.facade;

import com.rouby.auth.application.dto.request.LoginCommand;
import com.rouby.auth.application.dto.response.LoginInfo;
import com.rouby.auth.jwt.JwtTokenProvider;
import com.rouby.common.exception.CustomException;
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
public class AuthFacade {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional(readOnly = true)
  public LoginInfo login(LoginCommand command){

    User user = userRepository.findByEmail(command.email())
        .orElseThrow(() -> CustomException.from(UserErrorCode.INVALID_USER));

    if (!passwordEncoder.matches(command.password(), user.getPassword())) {
      throw CustomException.from(UserErrorCode.INVALID_USER_PASSWORD);
    }

    return new LoginInfo(jwtTokenProvider.createAccessToken(
        user.getId().toString(),
        user.getRole().toString(),
        user.getEmail()));
  }



}
