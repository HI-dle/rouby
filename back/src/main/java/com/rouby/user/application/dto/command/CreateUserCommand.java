package com.rouby.user.application.dto.command;

import lombok.Builder;

@Builder
public record CreateUserCommand(
    String email,
    String password
) {
}
