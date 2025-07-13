package com.rouby.user.presentation.dto.request;

import com.rouby.user.application.dto.command.ResetPasswordCommand;
import com.rouby.user.presentation.validation.ValidPassword;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ResetPasswordRequest(
    @NotBlank @Length(min = 8, max = 32) @ValidPassword String newPassword,
    @NotBlank String token,
    @NotBlank String email) {

  public ResetPasswordCommand toCommand() {
    return ResetPasswordCommand.builder()
        .newPassword(newPassword)
        .token(token)
        .build();
  }
}
