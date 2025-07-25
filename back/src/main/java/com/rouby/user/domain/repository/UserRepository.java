package com.rouby.user.domain.repository;

import com.rouby.user.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  User save(User user);

  Optional<User> findByIdAndDeletedAtIsNull(Long userId);

}
