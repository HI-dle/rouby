package com.rouby.user.user.application.dto.info;

public record TokenInfo(
    String accessToken,
    String refreshToken
) {

}
