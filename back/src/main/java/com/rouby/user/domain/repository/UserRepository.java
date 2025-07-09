package com.rouby.user.domain.repository;

import java.util.Optional;

import com.rouby.user.domain.entity.User;

public interface UserRepository {

  Optional<User> findByEmail(String email);
	Optional<User> findById(Long id);

}
