package com.rouby.user.application;

import com.rouby.common.utils.CodeGenerator;
import com.rouby.notification.application.service.EmailService;
import com.rouby.user.application.dto.SaveVerificationCodeCommand;
import com.rouby.user.application.dto.SendEmailVerificationCommand;
import com.rouby.user.application.dto.VerifyEmailCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final EmailService emailService;
  private final UserService userService;

  @Transactional
  public void sendEmailVerification(SendEmailVerificationCommand command) {
    String email = command.email();

    if (userService.alreadyExistsEmail(email)) {
      throw new IllegalArgumentException("이미 가입이 완료된 이메일입니다.");
    }

    String code = CodeGenerator.generateAlphaNumericCode(6);

    emailService.send(command.toSendEmailCommand(email, "[Rouby] 이메일 인증 코드입니다.", code));

    userService.saveVerificationCode(SaveVerificationCodeCommand.of(email, code));
  }

  @Transactional
  public void verifyEmail(VerifyEmailCommand command) {
    userService.verifyEmail(command);
  }
}
