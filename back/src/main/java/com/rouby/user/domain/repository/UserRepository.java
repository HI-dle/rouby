package com.rouby.user.domain.repository;

import com.rouby.user.domain.entity.User;

public interface UserRepository {
  Boolean existsByEmail(String email);
  User save(User user);
}
