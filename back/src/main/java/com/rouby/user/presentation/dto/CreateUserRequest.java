package com.rouby.user.presentation.dto;

import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.presentation.validation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record CreateUserRequest(
    @Email @NotBlank @Length(max = 100) String email,
    @NotBlank @Length(min=8, max=32) @ValidPassword String password
) {

  public CreateUserCommand toCommand() {
    return CreateUserCommand.builder()
        .email(email)
        .password(password)
        .build();
  }
}
