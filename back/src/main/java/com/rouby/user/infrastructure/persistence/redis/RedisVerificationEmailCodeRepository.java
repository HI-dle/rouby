package com.rouby.user.infrastructure.persistence.redis;

import com.rouby.user.application.entity.VerificationEmailCode;
import com.rouby.user.domain.repository.VerificationEmailCodeRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RedisVerificationEmailCodeRepository implements VerificationEmailCodeRepository {

  private static final String KEY = "verification:email:";
  private static final Duration ttl = Duration.ofMinutes(5);

  private final RedisTemplate<String, Object> redisTemplate;

  public void save(String email, VerificationEmailCode code) {
    redisTemplate.opsForValue().set(buildKey(email), code, ttl);
  }

  public Optional<VerificationEmailCode> findByEmail(String email) {
    VerificationEmailCode code = (VerificationEmailCode) redisTemplate.opsForValue()
        .get(buildKey(email));
    return Optional.of(code);
  }

  private String buildKey(String email) {
    return KEY + email;
  }
}
