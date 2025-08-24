package com.rouby.user.infrastructure.persistence.jpa;


import com.rouby.user.user.domain.entity.User;
import java.time.LocalTime;
import java.util.List;

public interface UserJpaRepositoryCustom {

  List<User> findActiveUsersWithBriefingNotification(LocalTime dailyStartTime);
}
