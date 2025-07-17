package com.rouby.user.presentation.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartsWithValidator implements ConstraintValidator<StartsWith, String> {

  private String prefix;

  @Override
  public void initialize(StartsWith constraint) {
    this.prefix = constraint.prefix();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && value.startsWith(prefix);
  }
}
