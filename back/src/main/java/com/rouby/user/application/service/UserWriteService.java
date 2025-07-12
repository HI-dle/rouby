package com.rouby.user.application.service;

import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.domain.service.UserPasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteService {

  private final UserRepository userRepository;
  private final UserPasswordEncoder passwordEncoder;

  @Transactional
  public void create(CreateUserCommand command) {
    //Todo. command.email() 인증 여부 확인

    if (userRepository.existsByEmail(command.email())) {
      throw UserException.of(UserErrorCode.DUPLICATE_EMAIL);
    }

    User user = User.create(command.email(), command.password(), passwordEncoder);
    userRepository.save(user);
  }
}
