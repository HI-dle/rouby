package com.rouby.user.presentation;

import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.request.LoginRequest;
import com.rouby.user.presentation.dto.response.LoginResponse;
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

  private final UserFacade userFacade;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    log.info("요청 받았습니다~!");
    log.info(request.email());
    return ResponseEntity.ok(LoginResponse.from(userFacade.login(request.toApplication())));
  }

}
