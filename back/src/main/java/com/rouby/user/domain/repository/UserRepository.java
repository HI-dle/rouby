package com.rouby.user.domain.repository;

import com.rouby.user.domain.entity.User;
import java.time.Duration;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findByEmail(String email);

  Optional<Long> getUserIdByToken(String token);

  Optional<User> findById(Long userId);

  void delete(String token);

  void storePasswordResetToken(String token, Long id, Duration tokenTtl);
}

