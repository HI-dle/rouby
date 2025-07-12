package com.rouby.user.presentation.dto.requset;

import com.rouby.user.application.dto.command.ResetPasswordCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;

@Builder
public record ResetPasswordRequest(
    @NotBlank @Length(min = 8, max = 32) String newPassword,
    Long userId) {

  public ResetPasswordCommand toCommand() {
    return ResetPasswordCommand.builder()
        .newPassword(newPassword)
        .userId(userId)
        .build();
  }
}
