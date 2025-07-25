package com.rouby.user.application.dto.command;

import lombok.Builder;

@Builder
public record ResetPasswordCommand(
    String currentPassword,
    String newPassword
) {

}
