package com.rouby.assistant.briefing.infrastructure.persistence.jpa;

import static com.rouby.assistant.briefing.domain.QBriefing.briefing;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.assistant.briefing.domain.Briefing;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * @Date : 2025. 07. 07.
 *
 * @author : hanjihoon
 */
@RequiredArgsConstructor
public class BriefingJpaRepositoryCustomImpl implements BriefingJpaRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public Optional<Briefing> findByUserIdAndDate(Long userId, LocalDate date) {
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.plusDays(1).atStartOfDay();

    Briefing briefingResult = queryFactory.selectFrom(briefing)
        .where(
            briefing.createdAt.goe(startOfDay)
                .and(briefing.createdAt.lt(endOfDay))
                .and(briefing.userId.eq(userId))
                .and(briefing.deletedAt.isNull())
        )
        .orderBy(briefing.createdAt.desc())
        .fetchFirst();

    return Optional.ofNullable(briefingResult);
  }
}
