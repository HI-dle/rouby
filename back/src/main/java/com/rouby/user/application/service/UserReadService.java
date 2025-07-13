package com.rouby.user.application.service;

import static com.rouby.user.application.exception.UserErrorCode.INVALID_USER;

import com.rouby.user.application.exception.UserException;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserReadService {

  private final UserRepository userRepository;

  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> UserException.from(INVALID_USER));
  }

  public boolean alreadyExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

}
