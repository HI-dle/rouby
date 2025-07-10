package com.rouby.user.infrastructure.persistence.redis;

import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRedisRepositoryImpl implements UserRedisRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  private static final String PASSWORD_RESET_PREFIX = "reset-password:";

  @Override
  public void storePasswordResetToken(String token, Long userId, Duration ttl) {
    redisTemplate.opsForValue().set(PASSWORD_RESET_PREFIX + token, userId.toString(), ttl);
  }

  @Override
  public Optional<Long> getUserIdByToken(String token) {
    return Optional.ofNullable(redisTemplate.opsForValue().get(PASSWORD_RESET_PREFIX + token))
        .map(Object::toString)
        .map(Long::valueOf);
  }

  @Override
  public void delete(String token) {
    redisTemplate.delete(PASSWORD_RESET_PREFIX + token);
  }

}
