package com.rouby.user.user.infrastructure.persistence.jpa;

import com.rouby.user.user.domain.entity.User;
import com.rouby.user.user.domain.repository.UserRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends
    JpaRepository<User, Long>, UserJpaRepositoryCustom, UserRepository {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);
}
