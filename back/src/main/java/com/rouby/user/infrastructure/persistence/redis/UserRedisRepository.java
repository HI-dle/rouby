package com.rouby.user.infrastructure.persistence.redis;

import java.time.Duration;
import java.util.Optional;

public interface UserRedisRepository {

  void storePasswordResetToken(String token, Long userId, Duration ttl);

  Optional<Long> getUserIdByToken(String token);

  void delete(String token);
}
