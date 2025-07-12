package com.rouby.user.application.service;

import static com.rouby.user.application.exception.UserErrorCode.INVALID_EMAIL_VERIFICATION;

import com.rouby.user.application.dto.SaveVerificationCodeCommand;
import com.rouby.user.application.dto.VerifyEmailCommand;
import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.domain.entity.User;
import com.rouby.user.application.service.verification.VerificationEmailCode;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.application.service.verification.VerificationEmailCodeStorage;
import com.rouby.user.domain.service.UserPasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteService {

  private final UserRepository userRepository;
  private final UserPasswordEncoder passwordEncoder;
  private final VerificationEmailCodeStorage verificationEmailCodeStorage;

  @Transactional
  public void create(CreateUserCommand command) {
    //Todo. command.email() 인증 여부 확인

    if (userRepository.existsByEmail(command.email())) {
      throw UserException.of(UserErrorCode.DUPLICATE_EMAIL);
    }

    User user = User.create(command.email(), command.password(), passwordEncoder);
    userRepository.save(user);
  }

  public void saveVerificationCode(SaveVerificationCodeCommand command) {
    verificationEmailCodeStorage.save(command.email(), command.toEntity());
  }

  public void deleteVerificationCode(String email) {
    verificationEmailCodeStorage.delete(email);
  }

  public void verifyEmail(VerifyEmailCommand command) {
    VerificationEmailCode code = verificationEmailCodeStorage.findByEmail(
        command.email()).orElseThrow(
        () -> UserException.from(INVALID_EMAIL_VERIFICATION));

    if (!command.code().equals(code.getCode())) {
      throw UserException.from(INVALID_EMAIL_VERIFICATION);
    }

    code.verified();
    verificationEmailCodeStorage.verified(command.email(), code);
  }
}
