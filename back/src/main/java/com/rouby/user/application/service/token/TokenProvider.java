package com.rouby.user.application.service.token;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 15.
 */
public interface TokenProvider {

  String createAccessToken(String userId, String role, String email);

  boolean validateAccessToken(String token);

  Long getUserId(String token);

  String getEmail(String token);

  String getRole(String token);

  String resolveAccessToken(HttpServletRequest request);

  String createVerificationToken(String email);

  boolean validateVerificationToken(String token);

  String extractEmail(String token);
}
