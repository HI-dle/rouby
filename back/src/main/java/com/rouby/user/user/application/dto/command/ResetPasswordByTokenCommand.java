package com.rouby.user.user.application.dto.command;

import lombok.Builder;

@Builder
public record ResetPasswordByTokenCommand(
    String newPassword,
    String token,
    String email
) {

}
