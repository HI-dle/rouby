package com.rouby.user.application.dto.command;

import lombok.Builder;

@Builder
public record ResetPasswordByTokenCommand(
    String newPassword,
    String token,
    String email
) {

}
