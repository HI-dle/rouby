package com.rouby.user.presentation;

import com.rouby.user.presentation.dto.request.CreateUserRequest;

public class UserRequestStub {

  static final String VALID_EMAIL = "hyezuu@email.com";
  static final String VALID_PASSWORD = "validPassword!";

  public static CreateUserRequest valid() {
    return new CreateUserRequest(VALID_EMAIL, VALID_PASSWORD);
  }

  public static CreateUserRequest withInvalidEmail() {
    return new CreateUserRequest("not-an-email", VALID_PASSWORD);
  }

  public static CreateUserRequest withInvalidPassword() {
    return new CreateUserRequest(VALID_EMAIL, "short");
  }
}

