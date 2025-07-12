package com.rouby.user.infrastructure.persistence.jpa;

import com.rouby.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends
    JpaRepository<User, Long>, UserJpaRepositoryCustom {

  boolean existsByEmail(String email);
  Optional<User> findByEmail(String email);
}
