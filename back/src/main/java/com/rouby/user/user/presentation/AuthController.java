package com.rouby.user.user.presentation;

import com.rouby.user.user.application.usecase.UserUsecase;
import com.rouby.user.user.presentation.dto.request.LoginRequest;
import com.rouby.user.user.presentation.dto.request.RefreshTokenRequest;
import com.rouby.user.user.presentation.dto.response.LoginResponse;
import com.rouby.user.user.presentation.dto.response.TokenResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {

  private final UserUsecase userUsecase;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(LoginResponse.from(userUsecase.login(request.toApplication())));
  }

  @PostMapping("/refresh")
  public ResponseEntity<TokenResponse> refresh(
      @RequestBody @Valid RefreshTokenRequest request) {
    return ResponseEntity.ok(TokenResponse.from(userUsecase.refresh(request.toApplication())));
  }
}
