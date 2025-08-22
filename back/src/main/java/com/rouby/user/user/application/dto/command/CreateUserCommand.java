package com.rouby.user.user.application.dto.command;

import lombok.Builder;

@Builder
public record CreateUserCommand(
    String email,
    String password,
    String token
) {
}
