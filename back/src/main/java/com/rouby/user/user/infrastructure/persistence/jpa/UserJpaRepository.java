package com.rouby.user.user.infrastructure.persistence.jpa;

import com.rouby.user.user.domain.entity.User;
import com.rouby.user.user.domain.repository.UserRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserJpaRepository extends
    JpaRepository<User, Long>, UserJpaRepositoryCustom, UserRepository {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  @Query("SELECT COUNT(rt) FROM RefreshToken rt WHERE rt.user = :user")
  long countRefreshTokensByUser(User user);
}
