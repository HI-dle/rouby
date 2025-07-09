package com.rouby.user.application;

import com.rouby.common.utils.CodeGenerator;
import com.rouby.notification.application.service.EmailService;
import com.rouby.user.presentation.dto.SendEmailVerificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final EmailService emailService;
  private final UserService userService;

  @Transactional
  public void sendEmailVerification(SendEmailVerificationRequest request) {
    String email = request.email();

    if (userService.alreadyExistsEmail(email)) {
      throw new IllegalArgumentException("이미 가입이 완료된 이메일입니다.");
    }

    String code = CodeGenerator.generateAlphaNumericCode(6);

    SendEmailCommand command = request.toCommand(email, "[Rouby] 이메일 인증 코드입니다.", code);
    emailService.send(command);

    // TODO: 레디스 저장
  }
}
