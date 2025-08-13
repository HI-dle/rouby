package com.rouby.user.domain.repository;

import com.rouby.user.application.dto.info.UserInfo;
import com.rouby.user.domain.entity.User;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  User save(User user);

  Optional<User> findByIdAndDeletedAtIsNull(Long userId);

  List<User> findActiveUsersWithBriefingNotification(LocalTime dailyStartTime);

}
