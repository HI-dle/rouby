package com.rouby.user.application.service;

import static com.rouby.user.application.exception.UserErrorCode.INVALID_EMAIL_VERIFICATION;
import static com.rouby.user.application.exception.UserErrorCode.INVALID_USER;

import com.rouby.common.exception.CustomException;
import com.rouby.user.application.dto.VerifyEmailCommand;
import com.rouby.user.application.entity.VerificationEmailCode;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.domain.repository.VerificationEmailCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserReadService {

  private final UserRepository userRepository;
  private final VerificationEmailCodeRepository verificationEmailCodeRepository;

  @Transactional(readOnly = true)
  public User findByEmail(String email) {
    return userRepository.findByEmail(email)
        .orElseThrow(() -> CustomException.from(INVALID_USER));
  }

  public boolean alreadyExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public void verifyEmail(VerifyEmailCommand command) {
    VerificationEmailCode code = verificationEmailCodeRepository.findByEmail(
        command.email()).orElseThrow(
        () -> UserException.from(INVALID_EMAIL_VERIFICATION));

    if (!command.code().equals(code.getCode())) {
      throw UserException.from(INVALID_EMAIL_VERIFICATION);
    }

    code.verified();
    verificationEmailCodeRepository.save(command.email(), code);
  }
}
