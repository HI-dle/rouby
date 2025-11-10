package com.rouby.user.user.application.service;

import static com.rouby.user.user.application.exception.UserErrorCode.DUPLICATE_EMAIL;
import static com.rouby.user.user.application.exception.UserErrorCode.EMAIL_NOT_VERIFIED;
import static com.rouby.user.user.application.exception.UserErrorCode.EMAIL_VERIFICATION_TOKEN_MISMATCH;
import static com.rouby.user.user.application.exception.UserErrorCode.EXPIRED_REFRESH_TOKEN;
import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_EMAIL_VERIFICATION;
import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_EMAIL_VERIFICATION_TOKEN;
import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_USER;
import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_USER_PASSWORD;
import static com.rouby.user.user.application.exception.UserErrorCode.ONBOARDING_STATE_CHANGE_NOT_ALLOWED;
import static com.rouby.user.user.application.exception.UserErrorCode.PASSWORD_TOKEN_EXPIRED;
import static com.rouby.user.user.application.exception.UserErrorCode.TOO_MANY_SESSIONS;
import static com.rouby.user.user.application.exception.UserErrorCode.USER_NOT_FOUND;

import com.rouby.common.props.SettingProperties;
import com.rouby.user.user.application.dto.command.CreateUserCommand;
import com.rouby.user.user.application.dto.command.FindPasswordCommand;
import com.rouby.user.user.application.dto.command.LoginCommand;
import com.rouby.user.user.application.dto.command.RefreshTokenCommand;
import com.rouby.user.user.application.dto.command.ResetPasswordByTokenCommand;
import com.rouby.user.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.user.application.dto.command.SaveVerificationCodeCommand;
import com.rouby.user.user.application.dto.command.UpdateUserInfoCommand;
import com.rouby.user.user.application.dto.command.UpdateUserRoubySettingCommand;
import com.rouby.user.user.application.dto.command.VerifyEmailCommand;
import com.rouby.user.user.application.dto.info.LoginInfo;
import com.rouby.user.user.application.dto.info.TokenInfo;
import com.rouby.user.user.application.exception.UserErrorCode;
import com.rouby.user.user.application.exception.UserException;
import com.rouby.user.user.application.service.token.TokenProvider;
import com.rouby.user.user.application.service.verification.VerificationEmailCode;
import com.rouby.user.user.application.service.verification.VerificationEmailCodeStorage;
import com.rouby.user.user.application.service.verification.VerificationPasswordTokenStorage;
import com.rouby.user.user.domain.entity.RefreshToken;
import com.rouby.user.user.domain.entity.User;
import com.rouby.user.user.domain.repository.UserRepository;
import com.rouby.user.user.domain.service.UserPasswordEncoder;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserWriteService {

  private final UserRepository userRepository;
  private final UserPasswordEncoder passwordEncoder;
  private final VerificationEmailCodeStorage verificationEmailCodeStorage;
  private final VerificationPasswordTokenStorage verificationPasswordCodeStorage;
  private final SettingProperties settingProperties;
  private final TokenProvider tokenProvider;

  @Transactional
  public void create(CreateUserCommand command) {
    ensureTokenMatchesEmail(command);
    ensureEmailNotTaken(command.email());
    ensureEmailIsVerified(command.email());

    User user = User.create(command.email(), command.password(), passwordEncoder);
    userRepository.save(user);
    verificationEmailCodeStorage.delete(command.email());
  }

  private void ensureTokenMatchesEmail(CreateUserCommand command) {
    if(!tokenProvider.validateVerificationToken(command.token())){
      throw UserException.from(INVALID_EMAIL_VERIFICATION_TOKEN);
    }

    if(!tokenProvider.extractEmail(command.token()).equals(command.email())){
      throw UserException.from(EMAIL_VERIFICATION_TOKEN_MISMATCH);
    }
  }

  private void ensureEmailNotTaken(String email) {
    if (userRepository.existsByEmail(email)) {
      throw UserException.from(DUPLICATE_EMAIL);
    }
  }

  private void ensureEmailIsVerified(String email) {
    VerificationEmailCode code = verificationEmailCodeStorage.findByEmail(email)
        .orElseThrow(() -> UserException.from(EMAIL_NOT_VERIFIED));

    if (!code.isVerified()) {
      throw UserException.from(EMAIL_NOT_VERIFIED);
    }
  }

  public void saveVerificationCode(SaveVerificationCodeCommand command) {
    verificationEmailCodeStorage.save(command.email(), command.toEntity());
  }

  public void deleteVerificationCode(String email) {
    verificationEmailCodeStorage.delete(email);
  }

  public void deletePasswordLinkCode(String email) {
    verificationPasswordCodeStorage.deleteByEmail(email);
  }

  public String verifyEmail(VerifyEmailCommand command) {
    VerificationEmailCode code = verificationEmailCodeStorage.findByEmail(
        command.email()).orElseThrow(
        () -> UserException.from(INVALID_EMAIL_VERIFICATION));

    if (!command.code().equals(code.getCode())) {
      throw UserException.from(INVALID_EMAIL_VERIFICATION);
    }

    verificationEmailCodeStorage.verified(command.email(), code.verified());
    return tokenProvider.createVerificationToken(command.email());
  }

  @Transactional
  public void resetPassword(Long userId, ResetPasswordCommand command) {
    User user = userRepository.findByIdAndDeletedAtIsNull(userId).orElseThrow(() ->
        UserException.from(UserErrorCode.USER_NOT_FOUND));


    if (!passwordEncoder.matches(command.currentPassword(), user.getPassword())) {
      throw UserException.from(UserErrorCode.INVALID_PASSWORD);
    }

    user.updatePassword(passwordEncoder, command.newPassword());
  }

  @Transactional
  public void resetPasswordByToken(ResetPasswordByTokenCommand command) {
    User user = userRepository.findByEmail(command.email())
        .orElseThrow(() -> UserException.from(USER_NOT_FOUND));

    validatePasswordToken(command.email(), command.token());

    user.updatePassword(passwordEncoder, command.newPassword());

    verificationPasswordCodeStorage.deleteByEmail(command.email());
  }

  @Transactional
  public void updateRoubySettings(Long userId, UpdateUserRoubySettingCommand command) {
    User user = userRepository.findByIdAndDeletedAtIsNull(userId).orElseThrow(() ->
        UserException.from(USER_NOT_FOUND));

    user.updateRoubySettings(command.toCommunicationTone(
            settingProperties.getCommunicationToneProperties().getMaxSize()),
        command.toNotificationSettings(user));
  }

  public String getResetPasswordLink(FindPasswordCommand command) {
    if (!userRepository.existsByEmail(command.email())) {
      throw UserException.from(USER_NOT_FOUND);
    }

    String code = UUID.randomUUID().toString();
    verificationPasswordCodeStorage.storePasswordResetToken(command.email(), code);

    return code;
  }

  public void validatePasswordToken(String email, String token) {
    String savedToken = verificationPasswordCodeStorage.getTokenByEmail(email)
        .orElseThrow(() -> UserException.from(PASSWORD_TOKEN_EXPIRED));

    if (!token.equals(savedToken)) {
      throw UserException.from(PASSWORD_TOKEN_EXPIRED);
    }
  }

  @Transactional
  public void completeInitialUserInfoSetting(Long id) {
    User user = userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> UserException.from(UserErrorCode.USER_NOT_FOUND));
    handleIllegalState(user::completeUserInfoSetting, ONBOARDING_STATE_CHANGE_NOT_ALLOWED);
  }

  @Transactional
  public void completeInitialRoubySetting(Long id) {
    User user = userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> UserException.from(UserErrorCode.USER_NOT_FOUND));
    handleIllegalState(user::completeRoubySetting, ONBOARDING_STATE_CHANGE_NOT_ALLOWED);
  }

  private void handleIllegalState(Runnable action, UserErrorCode errorCode) {
    try {
      action.run();
    } catch (IllegalStateException e) {
      throw UserException.from(errorCode);
    }
  }

  @Transactional
  public void updateUserInfo(UpdateUserInfoCommand command) {
    User user = userRepository.findByIdAndDeletedAtIsNull(command.updaterId()).orElseThrow(() ->
        UserException.from(UserErrorCode.USER_NOT_FOUND));
    user.updateUserInfo(
        command.nickname(), command.healthStatusKeywords(), command.profileKeywords(),
        command.dailyStartTime(), command.dailyEndTime());
  }

  @Transactional
  public void delete(Long userId) {
    User user = userRepository.findByIdAndDeletedAtIsNull(userId)
        .orElseThrow(() -> UserException.from(USER_NOT_FOUND));

    user.delete(userId);
  }

  @Transactional
  public LoginInfo validUser(LoginCommand command) {
    User user = userRepository.findByEmail(command.email())
        .orElseThrow(() -> UserException.from(INVALID_USER));

    if (!passwordEncoder.matches(command.password(), user.getPassword())) {
      throw UserException.from(INVALID_USER_PASSWORD);
    }

    long tokenCount = userRepository.countRefreshTokensByUser(user);

    if (tokenCount >= 10) {
      if (!command.isForceLogin()) {
        throw UserException.from(TOO_MANY_SESSIONS);
      } else {
        user.removeOldestRefreshTokenByExpiration();
      }
    }

    String token = tokenProvider.createRefreshToken();
    user.saveRefreshToken(passwordEncoder, token);

    return new LoginInfo(tokenProvider.createAccessToken(
        user.getId().toString(),
        user.getRole().toString(),
        user.getEmail()),
        token);
  }

  @Transactional
  public TokenInfo refresh(RefreshTokenCommand command) {
    String oldRefreshToken = command.refreshToken();

    String email = tokenProvider.getEmail(command.accessToken());

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> UserException.from(INVALID_USER));

    if (user.isExpiredRefreshToken(passwordEncoder, oldRefreshToken)) {
      throw UserException.from(EXPIRED_REFRESH_TOKEN);
    }

    String newAccessToken = tokenProvider.createAccessToken(
        user.getId().toString(),
        user.getRole().toString(),
        user.getEmail()
    );

    String newRefreshToken = tokenProvider.createRefreshToken();
    RefreshToken refreshToken = user.rotate(passwordEncoder, oldRefreshToken, newRefreshToken);

    return new TokenInfo(newAccessToken, refreshToken.getToken());
  }

  @Transactional
  public long deleteExpiredRefreshTokens(LocalDateTime cutoffTime) {
    return userRepository.deleteExpiredRefreshTokens(cutoffTime);
  }
}
