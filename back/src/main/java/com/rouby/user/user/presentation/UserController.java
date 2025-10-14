package com.rouby.user.user.presentation;

import com.rouby.user.user.application.usecase.UserUsecase;
import com.rouby.user.user.infrastructure.security.dto.SecurityUser;
import com.rouby.user.user.presentation.dto.request.CreateUserRequest;
import com.rouby.user.user.presentation.dto.request.FindPasswordRequest;
import com.rouby.user.user.presentation.dto.request.ResetPasswordByTokenRequest;
import com.rouby.user.user.presentation.dto.request.ResetPasswordRequest;
import com.rouby.user.user.presentation.dto.request.SendEmailVerificationRequest;
import com.rouby.user.user.presentation.dto.request.UpdateMyUserInfoRequest;
import com.rouby.user.user.presentation.dto.request.UpdateRoubySettingRequest;
import com.rouby.user.user.presentation.dto.response.RoubySettingResponse;
import com.rouby.user.user.presentation.dto.response.UserCheckResponse;
import com.rouby.user.user.presentation.dto.request.VerifyEmailRequest;
import com.rouby.user.user.presentation.dto.response.VerifyEmailTokenResponse;
import com.rouby.user.user.presentation.validation.StartsWith;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserUsecase userUsecase;

  @PostMapping("/email-verification/request")
  public ResponseEntity<Void> requestEmail(
      @RequestBody @Valid SendEmailVerificationRequest request) {
    userUsecase.sendEmailVerification(request.toCommand());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/email-verification/verify")
  public ResponseEntity<VerifyEmailTokenResponse> verifyEmail(
      @RequestBody @Valid VerifyEmailRequest request) {
    return ResponseEntity.ok(
        VerifyEmailTokenResponse.of(userUsecase.verifyEmail(request.toCommand())));
  }

  @PostMapping
  public ResponseEntity<Void> createUser(
      @RequestHeader("Authorization") @StartsWith(prefix = "EmailVerification ") String token,
      @RequestBody @Valid CreateUserRequest req) {
    userUsecase.createUser(req.toCommand(token));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PatchMapping("/password/reset/token")
  public ResponseEntity<Void> resetPasswordByToken(
      @RequestBody ResetPasswordByTokenRequest request) {
    userUsecase.resetPasswordByToken(request.toCommand());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/password/reset/validate")
  public ResponseEntity<Void> validateResetToken(@RequestParam String email,
      @RequestParam String token) {
    userUsecase.validatePasswordToken(email, token);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/password/find")
  public ResponseEntity<Void> findPassword(
      @RequestBody FindPasswordRequest request) {
    userUsecase.findPassword(request.toCommand());
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PatchMapping("/password/reset")
  public ResponseEntity<Void> resetPassword(
      @AuthenticationPrincipal SecurityUser securityUser,
      @RequestBody @Valid ResetPasswordRequest request) {
    userUsecase.resetPassword(securityUser.getId(), request.toCommand());
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping("/rouby-setting")
  public ResponseEntity<RoubySettingResponse> getRoubySetting(
      @AuthenticationPrincipal SecurityUser securityUser) {
    return ResponseEntity.ok(RoubySettingResponse.from(
        userUsecase.getRoubySettingInfo(securityUser.getId())));
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PutMapping("/rouby-setting")
  public ResponseEntity<Void> updateRoubySetting(
      @AuthenticationPrincipal SecurityUser securityUser,
      @RequestBody @Valid UpdateRoubySettingRequest request) {
    userUsecase.updateRoubySettings(securityUser.getId(), request.toCommand());
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/basic-info")
  @PreAuthorize("hasAnyRole('USER')")
  public ResponseEntity<UserCheckResponse> userInfoCheck(
      @AuthenticationPrincipal SecurityUser securityUser) {
    return ResponseEntity.ok(
        UserCheckResponse.from(userUsecase.userInfoCheck(securityUser.getId())));
  }

  @PatchMapping("/user-info")
  @PreAuthorize("hasAnyRole('USER')")
  public ResponseEntity<Void> updateUserInfo(
      @AuthenticationPrincipal SecurityUser securityUser,
      @RequestBody @Valid UpdateMyUserInfoRequest request
  ) {
    userUsecase.updateMyUserInfo(request.toCommand(securityUser.getId()));
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PatchMapping("/onboarding/user-info/complete")
  public ResponseEntity<Void> completeUserInfoSetting(
      @AuthenticationPrincipal SecurityUser securityUser) {
    userUsecase.completeInitialUserInfoSetting(securityUser.getId());
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PatchMapping("/onboarding/rouby/complete")
  public ResponseEntity<Void> completeRoubySetting(
      @AuthenticationPrincipal SecurityUser securityUser) {
    userUsecase.completeInitialRoubySetting(securityUser.getId());
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PatchMapping("/delete")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal SecurityUser securityUser) {
    userUsecase.delete(securityUser.getId());
    return ResponseEntity.noContent().build();
  }
}
