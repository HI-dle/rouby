package com.rouby.user.presentation;

import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.CreateUserRequest;
import com.rouby.user.presentation.dto.SendEmailVerificationRequest;
import com.rouby.user.presentation.dto.request.FindPasswordRequest;
import com.rouby.user.presentation.dto.request.ResetPasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PostMapping("/email-verification/request")
  public ResponseEntity<Void> requestEmail(
      @RequestBody @Valid SendEmailVerificationRequest request) {
    userFacade.sendEmailVerification(request.toCommand());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/email-verification/verify")
  public ResponseEntity<Void> verifyEmail(
      @RequestBody @Valid VerifyEmailRequest request) {
    userFacade.verifyEmail(request.toCommand());
    return ResponseEntity.ok().build();
  }

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest req) {
    userFacade.createUser(req.toCommand());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PatchMapping("/password/reset/token")
  public ResponseEntity<Void> resetPasswordByToken(
      @RequestBody ResetPasswordRequest request) {

    userFacade.resetPasswordByToken(request.toCommand());

    return ResponseEntity.noContent().build();
  }

  @GetMapping("/password/reset/validate")
  public ResponseEntity<Void> validateResetToken(@RequestParam String email,
      @RequestParam String token) {

    userFacade.validatePasswordToken(email, token);

    return ResponseEntity.ok().build();
  }

  @PostMapping("/password/find")
  public ResponseEntity<Void> findPassword(
      @RequestBody FindPasswordRequest request) {

    userFacade.findPassword(request.toCommand());
    return ResponseEntity.noContent().build();
  }


  @PatchMapping("/password/reset")
  public ResponseEntity<Void> resetPassword(
      @AuthenticationPrincipal UserDetailsImpl userDetails,
      @RequestBody @Valid ResetPasswordRequest request) {

    userFacade.resetPassword(userDetails.getId(), request.toCommand());

    return ResponseEntity.ok().build();
  }
}
