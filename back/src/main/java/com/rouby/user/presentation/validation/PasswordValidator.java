package com.rouby.user.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

  private static final String PASSWORD_PATTERN =
      "^(?![A-Za-z]+$)(?!\\d+$)(?![\\W_]+$)[A-Za-z\\d\\W_]{8,32}$";

  private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

  @Override
  public boolean isValid(String password, ConstraintValidatorContext context) {
    return password == null || pattern.matcher(password).matches();
  }
}

