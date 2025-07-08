package com.rouby.user.domain.repository;

import com.rouby.user.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findByEmail(String email);

  //테스트용입니당
  void saveUser(User user);
}
