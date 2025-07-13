package com.rouby.user.infrastructure.persistence.redis;

import com.rouby.user.application.service.verification.VerificationEmailCode;
import com.rouby.user.application.service.verification.VerificationEmailCodeStorage;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisVerificationEmailCodeStorage implements VerificationEmailCodeStorage {

  private static final String KEY = "verification:email:";
  private static final Duration ttl = Duration.ofMinutes(5);
  private static final Duration verifiedTtl = Duration.ofMinutes(60);

  private final RedisTemplate<String, Object> redisTemplate;

  public void save(String email, VerificationEmailCode code) {
    redisTemplate.opsForValue().set(buildKey(email), code, ttl);
  }

  public void verified(String email, VerificationEmailCode code) {
    redisTemplate.opsForValue().set(buildKey(email), code, verifiedTtl);
  }

  public Optional<VerificationEmailCode> findByEmail(String email) {
    VerificationEmailCode code = (VerificationEmailCode) redisTemplate.opsForValue()
        .get(buildKey(email));
    return Optional.ofNullable(code);
  }

  public void delete(String email) {
    redisTemplate.delete(buildKey(email));
  }

  private String buildKey(String email) {
    return KEY + email;
  }
}
