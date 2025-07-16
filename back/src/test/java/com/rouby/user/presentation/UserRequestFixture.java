package com.rouby.user.presentation;

import com.rouby.user.presentation.dto.request.CreateUserRequest;
import com.rouby.user.presentation.dto.request.SendEmailVerificationRequest;
import com.rouby.user.presentation.dto.request.VerifyEmailRequest;

public class UserRequestFixture {

  static final String VALID_EMAIL = "hyezuu@email.com";
  static final String VALID_PASSWORD = "validPassword!";
  static final String INVALID_EMAIL = "invalid-email";
  static final String INVALID_PASSWORD = "short";
  static final String VALID_EMAIL_CODE = "VERIFY";
  static final String INVALID_EMAIL_CODE = "INVALID_CODE";
  static final String VALID_EMAIL_TOKEN = "EmailVerification "
      + "KZubXI78rtW5PUIlFUHEl/S2obaHKQ5SG4qYT7lbQX5Oy7/Rr4x3jXUhkhnm2qcP"
      + ".7vGGCif9isavID1Upp9siCuYh4o2gz6XEoN7y0LB4kY=";
  static final String INVALID_EMAIL_TOKEN = "invalid-token";

  public static CreateUserRequest toCreateRequest() {
    return new CreateUserRequest(VALID_EMAIL, VALID_PASSWORD);
  }

  public static CreateUserRequest toCreateRequestInvalidEmail() {
    return new CreateUserRequest(INVALID_EMAIL, VALID_PASSWORD);
  }

  public static CreateUserRequest toCreateRequestInvalidPassword() {
    return new CreateUserRequest(VALID_EMAIL, INVALID_PASSWORD);
  }

  public static SendEmailVerificationRequest toSendEmailVerificationRequest() {
    return new SendEmailVerificationRequest(VALID_EMAIL);
  }

  public static SendEmailVerificationRequest toSendEmailVerificationRequestInvalidEmail() {
    return new SendEmailVerificationRequest(INVALID_EMAIL);
  }

  public static VerifyEmailRequest toVerifyEmailRequest() {
    return new VerifyEmailRequest(VALID_EMAIL, VALID_EMAIL_CODE);
  }

  public static VerifyEmailRequest toVerifyEmailRequestInvalidEmail() {
    return new VerifyEmailRequest(INVALID_EMAIL, VALID_EMAIL_CODE);
  }

  public static VerifyEmailRequest toVerifyEmailRequestInvalidCode() {
    return new VerifyEmailRequest(VALID_EMAIL, INVALID_EMAIL_CODE);
  }
}

