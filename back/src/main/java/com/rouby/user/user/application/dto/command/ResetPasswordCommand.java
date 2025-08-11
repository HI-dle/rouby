package com.rouby.user.user.application.dto.command;

import lombok.Builder;

@Builder
public record ResetPasswordCommand(
    String currentPassword,
    String newPassword
) {

}
