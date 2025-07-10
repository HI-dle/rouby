package com.rouby.user.presentation;

import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.requset.FindPasswordRequest;
import com.rouby.user.presentation.dto.requset.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PatchMapping("/reset-password-by-token")
  public ResponseEntity<Void> resetPasswordByToken(
      @RequestParam("token") String token, @RequestBody ResetPasswordRequest request) {

    userFacade.resetPasswordByToken(token, request.toCommand());

    return ResponseEntity.ok().build();
  }

  @PatchMapping("/find-password")
  public ResponseEntity<Void> findPassword(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody FindPasswordRequest request) {

    userFacade.findPassword(request.toCommand());

    return ResponseEntity.ok().build();
  }

}
