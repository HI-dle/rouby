package com.rouby.user.user.infrastructure.persistence.jpa;

import com.rouby.user.user.domain.entity.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserJpaRepositoryCustom {

  List<User> findActiveUsersWithBriefingNotification(LocalTime dailyStartTime);

  Optional<User> findByRefreshToken(String token);

  long deleteExpiredRefreshTokens(LocalDateTime cutoffTime);
}
