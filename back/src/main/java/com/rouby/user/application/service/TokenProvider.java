package com.rouby.user.application.service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author : hanjihoon
 * @Date : 2025. 07. 15.
 */
public interface TokenProvider {

  String createAccessToken(String userId, String role, String email);

  boolean validateToken(String token);

  Long getUserId(String token);

  String getEmail(String token);

  String getRole(String token);

  String removeBearerPrefix(String token);

  String resolveToken(HttpServletRequest request);


}
