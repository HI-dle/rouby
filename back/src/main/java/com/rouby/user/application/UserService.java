package com.rouby.user.application;

import com.rouby.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public boolean alreadyExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }
}
