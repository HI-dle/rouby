package com.rouby.user.infrastructure.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
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
public class JwtAuthTokenProvider {
  public static final String BEARER_PREFIX = "Bearer ";
  private static final String CLAIM_ROLE_KEY = "role";
  private static final String CLAIM_EMAIL_KEY = "email";

  private final SecretKey key;
  private final long tokenTime;

  public JwtAuthTokenProvider(
      @Value("${jwt.secret}") String secretKey,
      @Value("${jwt.expiration}") long tokenTime) {
    this.tokenTime = tokenTime;
    this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String createAccessToken(String userId, String role, String email) {
    return createToken(userId, role, email, tokenTime);
  }


  public String resolveAccessToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith(BEARER_PREFIX)) {
      return bearerToken;
    }
    return null;
  }


  public boolean validateAccessToken(String token) {
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


  public Long getUserId(String token) {
    return Long.parseLong(getClaimFromToken(token, Claims::getSubject));
  }


  public String getEmail(String token) {
    return getClaimFromToken(token, claims -> claims.get(CLAIM_EMAIL_KEY, String.class));
  }


  public String getRole(String token) {
    return getClaimFromToken(token, claims -> claims.get(CLAIM_ROLE_KEY, String.class));
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

  private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
    Claims claims = Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(removeBearerPrefix(token))
        .getPayload();
    return claimsResolver.apply(claims);
  }

  private String removeBearerPrefix(String token) {
    if (token != null && token.startsWith(BEARER_PREFIX)) {
      return token.substring(BEARER_PREFIX.length());
    }
    return token;
  }
}
