package com.rouby.auth.presentation;

import com.rouby.auth.application.facade.AuthFacade;
import com.rouby.auth.presentation.dto.request.LoginRequest;
import com.rouby.auth.presentation.dto.response.LoginResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class AuthController {

  private final AuthFacade authFacade;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
    return ResponseEntity.ok(LoginResponse.from(authFacade.login(request.toApplication())));
  }

}
