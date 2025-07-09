package com.rouby.auth.presentation;

import com.rouby.auth.application.facade.AuthFacade;
import com.rouby.auth.presentation.dto.request.LoginRequest;
import com.rouby.auth.presentation.dto.response.LoginResponse;
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

  private final AuthFacade authFacade;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    log.info("요청 받았습니다~!");
    log.info(request.email());
    log.info(request.password());
    return ResponseEntity.ok(LoginResponse.from(authFacade.login(request.toApplication())));
  }

}
