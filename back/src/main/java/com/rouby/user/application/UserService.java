package com.rouby.user.application;

import com.rouby.user.application.dto.SaveVerificationCodeCommand;
import com.rouby.user.application.dto.VerifyEmailCommand;
import com.rouby.user.application.entity.VerificationEmailCode;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.domain.repository.VerificationEmailCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final VerificationEmailCodeRepository verificationEmailCodeRepository;

  public boolean alreadyExistsEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  public void saveVerificationCode(SaveVerificationCodeCommand command) {
    verificationEmailCodeRepository.save(command.email(), command.toEntity());
  }

  @Transactional
  public void verifyEmail(VerifyEmailCommand command) {
    VerificationEmailCode code = verificationEmailCodeRepository.findByEmail(
        command.email()).orElseThrow(
        () -> new IllegalArgumentException("인증번호가 올바르지 않거나 유효시간(5분)이 경과되었습니다. 다시 확인해주세요."));

    if (!command.code().equals(code.getCode())) {
      throw new IllegalArgumentException("인증번호가 올바르지 않거나 유효시간(5분)이 경과되었습니다. 다시 확인해주세요.");
    }

    code.verified();
    verificationEmailCodeRepository.save(command.email(), code);
  }

  public void deleteVerificationCode(String email) {
    verificationEmailCodeRepository.delete(email);
  }
}
