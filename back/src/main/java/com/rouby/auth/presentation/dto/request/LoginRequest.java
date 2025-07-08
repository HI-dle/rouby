package com.rouby.auth.presentation.dto.request;

import com.rouby.auth.application.dto.request.LoginCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Builder
public record LoginRequest(@Email(message = "올바른 이메일 형식이 아닙니다.")
                           @NotBlank(message = "이메일은 필수입니다.")
                           String email,

                           @NotBlank(message = "비밀번호는 필수입니다.")
                           String password) {

  public LoginCommand toApplication(){
  return new LoginCommand(email, password);
  }
}

