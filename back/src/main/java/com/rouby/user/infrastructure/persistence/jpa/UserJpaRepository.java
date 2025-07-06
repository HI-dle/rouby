package com.rouby.user.infrastructure.persistence.jpa;

import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends
    JpaRepository<User, Long>, UserJpaRepositoryCustom, UserRepository {

}
