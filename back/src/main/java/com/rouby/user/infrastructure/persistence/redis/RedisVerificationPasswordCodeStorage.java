package com.rouby.user.infrastructure.persistence.redis;

import com.rouby.user.application.service.verification.VerificationPasswordCodeStorage;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisVerificationPasswordCodeStorage implements VerificationPasswordCodeStorage {

  private static final String PREFIX = "reset-password:";
  private static final Duration ttl = Duration.ofMinutes(20);

  private final RedisTemplate<String, Object> redisTemplate;

  @Override
  public void storePasswordResetToken(String email, String token) {
    redisTemplate.opsForValue().set(PREFIX + email, token, ttl);
  }

  @Override
  public Optional<String> getTokenByEmail(String email) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(PREFIX + email))
        .map(Object::toString);
  }

  @Override
  public void deleteByEmail(String email) {
    redisTemplate.delete(PREFIX + email);
  }
}
