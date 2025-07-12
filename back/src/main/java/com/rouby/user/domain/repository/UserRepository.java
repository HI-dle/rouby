package com.rouby.user.domain.repository;

import com.rouby.user.domain.entity.User;
import java.time.Duration;
import java.util.Optional;

public interface UserRepository {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  User save(User user);

  Optional<Long> getUserIdByToken(String token);

  Optional<User> findById(Long userId);

  void delete(String token);

  void storePasswordResetToken(String token, Long id, Duration tokenTtl);
}
