package com.rouby.user.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {
  String message() default "비밀번호는 영문/숫자/특수문자 중 2가지 이상 조합이어야 합니다.";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}