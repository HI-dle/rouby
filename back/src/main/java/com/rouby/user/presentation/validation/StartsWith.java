package com.rouby.user.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartsWithValidator.class)
public @interface StartsWith {
  String prefix();
  String message() default "잘못된 토큰 형식입니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
