package com.rouby.user.domain.repository;

public interface UserRepository {

  boolean existsByEmail(String email);

}
