package com.rouby.user.application.dto.response;

import lombok.Builder;

@Builder
public record ValidateTokenInfo(Long userId) {
}
