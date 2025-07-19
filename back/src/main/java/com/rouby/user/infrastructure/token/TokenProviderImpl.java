package com.rouby.user.infrastructure.token;

import com.rouby.user.application.service.token.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

  private final JwtAuthTokenProvider jwtAuthTokenProvider;
  private final AesHmacVerificationTokenProvider aesHmacVerificationTokenProvider;

  @Override
  public String createAccessToken(String userId, String role, String email) {
    return jwtAuthTokenProvider.createAccessToken(userId, role, email);
  }

  @Override
  public boolean validateAccessToken(String token) {
    return jwtAuthTokenProvider.validateAccessToken(token);
  }

  @Override
  public Long getUserId(String token) {
    return jwtAuthTokenProvider.getUserId(token);
  }

  @Override
  public String getEmail(String token) {
    return jwtAuthTokenProvider.getEmail(token);
  }

  @Override
  public String getRole(String token) {
    return jwtAuthTokenProvider.getRole(token);
  }

  @Override
  public String resolveAccessToken(HttpServletRequest request) {
    return jwtAuthTokenProvider.resolveAccessToken(request);
  }

  @Override
  public String createVerificationToken(String email) {
    return aesHmacVerificationTokenProvider.createVerificationToken(email);
  }

  @Override
  public boolean validateVerificationToken(String token) {
    return aesHmacVerificationTokenProvider.validateVerificationToken(token);
  }

  @Override
  public String extractEmail(String token) {
    return aesHmacVerificationTokenProvider.extractEmail(token);
  }
}
