package com.rouby.user.presentation;

import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.CreateUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

  private final UserFacade userFacade;

  @PostMapping
  public ResponseEntity<Void> createUser(@RequestBody @Valid CreateUserRequest req) {
    userFacade.createUser(req.toCommand());
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }
}
