package com.rouby.user.application;

import static com.rouby.user.application.exception.UserErrorCode.DUPLICATE_EMAIL;

import com.rouby.common.utils.CodeGenerator;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.notification.email.application.service.EmailService;
import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.application.dto.command.LoginCommand;
import com.rouby.user.application.dto.command.SaveVerificationCodeCommand;
import com.rouby.user.application.dto.command.SendEmailVerificationCommand;
import com.rouby.user.application.dto.command.VerifyEmailCommand;
import com.rouby.user.application.dto.info.LoginInfo;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.application.service.UserReadService;
import com.rouby.user.application.service.UserWriteService;
import com.rouby.user.domain.entity.User;
import com.rouby.user.infrastructure.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserReadService userReadService;
  private final UserWriteService userWriteService;
  private final EmailService emailService;
  private final JwtTokenProvider jwtTokenProvider;

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

  public void verifyEmail(VerifyEmailCommand command) {
    userWriteService.verifyEmail(command);
  }

  public void createUser(CreateUserCommand command) {
    userWriteService.create(command);
  }

  @Transactional(readOnly = true)
  public LoginInfo login(LoginCommand command){
    User user = userReadService.validUser(command);

    return new LoginInfo(jwtTokenProvider.createAccessToken(
        user.getId().toString(),
        user.getRole().toString(),
        user.getEmail()));
  }


}
