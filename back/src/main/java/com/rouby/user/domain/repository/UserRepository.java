package com.rouby.user.domain.repository;

import com.rouby.user.domain.entity.User;
import java.util.Optional;

public interface UserRepository {

  Optional<User> findByEmail(String email);
  
  Optional<User> findById(Long id);

}
