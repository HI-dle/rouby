package com.rouby.user.infrastructure.persistence.jpa;

import static com.rouby.user.domain.entity.NotificationType.BRIEFING;
import static com.rouby.user.domain.entity.QNotificationSetting.notificationSetting;
import static com.rouby.user.domain.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.user.application.dto.info.UserInfo;
import com.rouby.user.domain.entity.NotificationType;
import com.rouby.user.domain.entity.User;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserJpaRepositoryCustomImpl implements UserJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public List<User> findActiveUsersWithBriefingNotification(LocalTime dailyStartTime) {
    return jpaQueryFactory
        .selectFrom(user)
        .join(user.notificationSettings, notificationSetting)
        .where(
            user.deletedAt.isNull(),
            user.dailyActiveTime.dailyStartTime.eq(dailyStartTime),
            notificationSetting.notificationType.eq(BRIEFING),
            notificationSetting.isEnabled.isTrue()
        )
        .fetch();
  }
}
