package com.rouby.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Component
@Slf4j
public class JwtTokenProvider {
  public static final String BEARER_PREFIX = "Bearer ";
  private static final String CLAIM_ROLE_KEY = "role";
  private static final String CLAIM_EMAIL_KEY = "email";


  private final SecretKey key;
  private final long tokenTime;

  public JwtTokenProvider(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.expiration}") long tokenTime) {
    this.tokenTime = tokenTime;
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String createAccessToken(String userId, String role, String email) {
    String token = createToken(userId, role, email, tokenTime);
    return addBearerPrefix(token);
  }

  private String createToken(String userId, String role, String email, long expiration) {
    return Jwts.builder()
        .subject(userId)
        .claim(CLAIM_ROLE_KEY, role)
        .claim(CLAIM_EMAIL_KEY, email)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + expiration))
        .signWith(key)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(removeBearerPrefix(token));
      return true;
    } catch (ExpiredJwtException e) {
      log.warn("만료된 토큰 입니다.");
      return false;
    } catch (JwtException e) {
      log.warn("정상적인 토큰이 아닙니다.");
      return false;
    }
  }

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(removeBearerPrefix(token))
        .getPayload();
    return claimsResolver.apply(claims);
  }

  private String addBearerPrefix(String token) {
    return BEARER_PREFIX + token;
  }

  public String removeBearerPrefix(String token) {
    if (token != null && token.startsWith(BEARER_PREFIX)) {
      return token.substring(BEARER_PREFIX.length());
    }
    return token;
  }
}
