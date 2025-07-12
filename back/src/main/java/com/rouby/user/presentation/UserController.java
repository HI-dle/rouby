package com.rouby.user.presentation;

import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.requset.FindPasswordRequest;
import com.rouby.user.presentation.dto.requset.ResetPasswordRequest;
import com.rouby.user.presentation.dto.response.ValidateTokenResponse;
import com.rouby.user.presentation.dto.CreateUserRequest;
import com.rouby.user.presentation.dto.SendEmailVerificationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<ValidateTokenResponse> validateResetToken(@RequestParam String token) {

    return ResponseEntity.ok(ValidateTokenResponse
        .from(userFacade.validatePasswordToken(token)));
  }

  @PostMapping("/password/find")
  public ResponseEntity<Void> findPassword(
      @RequestBody FindPasswordRequest request) {

    userFacade.findPassword(request.toCommand());
    return ResponseEntity.noContent().build();
  }


}
