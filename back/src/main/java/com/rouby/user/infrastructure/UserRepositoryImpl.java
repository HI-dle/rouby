package com.rouby.user.infrastructure;

import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import com.rouby.user.infrastructure.persistence.jpa.UserJpaRepository;
import com.rouby.user.infrastructure.persistence.redis.UserRedisRepository;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

  private final UserJpaRepository userJpaRepository;
  private final UserRedisRepository userRedisRepository;


  @Override
  public Optional<User> findByEmail(String email) {
    return userJpaRepository.findByEmail(email);
  }

  @Override
  public Optional<Long> getUserIdByToken(String token) {
    return userRedisRepository.getUserIdByToken(token);
  }

  @Override
  public Optional<User> findById(Long userId) {
    return userJpaRepository.findById(userId);
  }

  @Override
  public void delete(String token) {
    userRedisRepository.delete(token);
  }

  @Override
  public void storePasswordResetToken(String token, Long userId, Duration ttl) {
    userRedisRepository.storePasswordResetToken(token, userId, ttl);
  }


}
