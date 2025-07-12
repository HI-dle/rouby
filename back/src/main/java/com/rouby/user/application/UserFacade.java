package com.rouby.user.application;

import static com.rouby.user.application.exception.UserErrorCode.DUPLICATE_EMAIL;

import com.rouby.common.utils.CodeGenerator;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.notification.email.application.service.EmailService;
import com.rouby.user.application.dto.SaveVerificationCodeCommand;
import com.rouby.user.application.dto.SendEmailVerificationCommand;
import com.rouby.user.application.dto.VerifyEmailCommand;
import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.application.service.UserReadService;
import com.rouby.user.application.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserReadService userReadService;
  private final UserWriteService userWriteService;
  private final EmailService emailService;

  public void sendEmailVerification(SendEmailVerificationCommand command) {
    String email = command.email();

    if (userReadService.alreadyExistsEmail(email)) {
      throw UserException.from(DUPLICATE_EMAIL);
    }

    String code = CodeGenerator.generateAlphaNumericCode(6);
    userWriteService.saveVerificationCode(SaveVerificationCodeCommand.of(email, code));

    try {
      emailService.send(command.toSendEmailCommand(email, code));
    } catch (EmailException e) {
      userWriteService.deleteVerificationCode(email);
      throw e;
    }
  }

  @Transactional
  public void verifyEmail(VerifyEmailCommand command) {
    userReadService.verifyEmail(command);
  }

  public void createUser(CreateUserCommand command) {
    userWriteService.create(command);
  }
}
