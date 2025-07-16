package com.rouby.user.application;


import static com.rouby.user.application.exception.UserErrorCode.DUPLICATE_EMAIL;

import com.rouby.common.props.URIProperty;
import com.rouby.common.utils.CodeGenerator;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.notification.email.application.service.EmailService;
import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.application.dto.command.LoginCommand;
import com.rouby.user.application.dto.command.ResetPasswordByTokenCommand;
import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.application.dto.command.SaveVerificationCodeCommand;
import com.rouby.user.application.dto.command.SendEmailVerificationCommand;
import com.rouby.user.application.dto.command.VerifyEmailCommand;
import com.rouby.user.application.dto.info.LoginInfo;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.application.service.UserReadService;
import com.rouby.user.application.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserReadService userReadService;
  private final UserWriteService userWriteService;
  private final EmailService emailService;
  private final URIProperty uriProperty;

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

  public String verifyEmail(VerifyEmailCommand command) {
    return userWriteService.verifyEmail(command);
  }

  public void createUser(CreateUserCommand command) {
    userWriteService.create(command);
  }

  public void findPassword(FindPasswordCommand command) {

    String token = userWriteService.getResetPasswordLink(command);

    String resetPasswordLink = uriProperty.generateResetPasswordLink(command.email(), token);

    try {
      emailService.send(command.toSendEmailCommand(command.email(), resetPasswordLink));
    } catch (EmailException e) {
      userWriteService.deletePasswordLinkCode(command.email());
      throw e;
    }
  }

  public void resetPasswordByToken(ResetPasswordByTokenCommand command) {
    userWriteService.resetPasswordByToken(command);
  }

  public void validatePasswordToken(String email, String token) {
    userWriteService.validatePasswordToken(email, token);
  }

  public void resetPassword(Long userId, ResetPasswordCommand command) {
    userWriteService.resetPassword(userId, command);
  }

  public LoginInfo login(LoginCommand command){
    return userReadService.validUser(command);
  }
}
