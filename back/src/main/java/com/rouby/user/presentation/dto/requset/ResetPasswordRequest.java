package com.rouby.user.presentation.dto.requset;

import com.rouby.user.application.dto.command.ResetPasswordCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ResetPasswordRequest(
    @NotBlank @Length(min = 8, max = 32) String currentPassword,
    @NotBlank @Length(min = 8, max = 32) String newPassword) {

  public ResetPasswordCommand toCommand() {
    return ResetPasswordCommand.builder()
        .currentPassword(currentPassword)
        .newPassword(newPassword)
        .build();
  }
}
