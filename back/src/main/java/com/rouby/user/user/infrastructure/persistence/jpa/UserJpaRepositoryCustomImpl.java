package com.rouby.user.user.infrastructure.persistence.jpa;


import static com.rouby.user.user.domain.entity.NotificationType.BRIEFING;
import static com.rouby.user.user.domain.entity.QNotificationSetting.notificationSetting;
import static com.rouby.user.user.domain.entity.QRefreshToken.refreshToken;
import static com.rouby.user.user.domain.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.user.user.domain.entity.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

  @Override
  public Optional<User> findByRefreshToken(String token) {
    User result = jpaQueryFactory
        .selectFrom(user)
        .join(user.refreshTokens, refreshToken).fetchJoin()
        .where(
            refreshToken.token.eq(token),
            user.deletedAt.isNull()
        )
        .fetchOne();

    return Optional.ofNullable(result);
  }

  @Override
  public long deleteExpiredRefreshTokens(LocalDateTime cutoffTime) {
    return jpaQueryFactory
        .delete(refreshToken)
        .where(refreshToken.expiredAt.before(cutoffTime))
        .execute();
  }
}
