package com.rouby.schedule.infrastructure.persistence.jpa;

import static com.rouby.common.jpa.QuerydslUtil.nullSafeBuilder;
import static com.rouby.schedule.domain.entity.QSchedule.schedule;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.schedule.domain.entity.QSchedule;
import com.rouby.schedule.domain.enums.OverrideType;
import com.rouby.schedule.domain.repository.criteria.GetScheduleCriteria;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides.ScheduleOverride;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleJpaRepositoryCustomImpl implements ScheduleJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  private final QSchedule child = new QSchedule("child");

  @Override
  public void bulkCancel(Long parentScheduleId, LocalDateTime fromAt) {
    jpaQueryFactory.update(schedule)
        .set(schedule.overrideInfo.overrideType, OverrideType.CANCELLED)
        .where(
            schedule.parentSchedule.id.eq(parentScheduleId),
            schedule.period.startAt.goe(fromAt),
            schedule.deletedAt.isNull()
        )
        .execute();
  }

  @Override
  public List<ScheduleWithOverrides> findSchedulesByCriteria(GetScheduleCriteria criteria) {

    List<Tuple> tuples = jpaQueryFactory.select(
            schedule.id,
            schedule.userId,
            schedule.title,
            schedule.memo,
            schedule.period.startAt,
            schedule.period.endAt,
            schedule.routineOffsetDays,
            schedule.alarmOffsetType,
            schedule.recurrenceRule,
            child.id,
            child.userId,
            child.title,
            child.memo,
            child.period.startAt,
            child.period.endAt,
            child.routineOffsetDays,
            child.alarmOffsetType,
            child.overrideInfo.overrideType,
            child.overrideInfo.overrideDate)
        .from(schedule)
        .leftJoin(child)
        .on(
            schedule.recurrenceRule.isNotNull()
                .and(child.deletedAt.isNull())
                .and(child.parentSchedule.eq(schedule))
                .and(startAtBeforeToAt(child, criteria.toAt()))
                .and(endAtAfterFromAt(child, criteria.fromAt()))
        )
        .where(buildWhereClause(schedule, criteria))
        .orderBy(schedule.userId.asc(), schedule.period.startAt.asc())
        .fetch();

    return convertGroupedList(tuples);
  }

  private List<ScheduleWithOverrides> convertGroupedList(List<Tuple> tuples) {
    return tuples.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.groupingBy(
                tuple -> tuple.get(schedule.id),
                LinkedHashMap::new,
                Collectors.collectingAndThen(
                    Collectors.toList(),
                    rows -> {
                      Tuple first = rows.get(0);

                      List<ScheduleOverride> children = rows.stream()
                          .filter(t -> t.get(child.id) != null)
                          .map(t -> ScheduleOverride.builder()
                              .id(t.get(child.id))
                              .userId(t.get(child.userId))
                              .title(t.get(child.title))
                              .memo(t.get(child.memo))
                              .startAt(t.get(child.period.startAt))
                              .endAt(t.get(child.period.endAt))
                              .routineOffsetDays(t.get(child.routineOffsetDays))
                              .alarmOffsetType(t.get(child.alarmOffsetType))
                              .overrideType(t.get(child.overrideInfo.overrideType))
                              .overrideDate(t.get(child.overrideInfo.overrideDate))
                              .build()
                          )
                          .sorted(Comparator.comparing(ScheduleOverride::startAt))
                          .toList();

                      return ScheduleWithOverrides.builder()
                          .id(first.get(schedule.id))
                          .userId(first.get(schedule.userId))
                          .title(first.get(schedule.title))
                          .memo(first.get(schedule.memo))
                          .startAt(first.get(schedule.period.startAt))
                          .endAt(first.get(schedule.period.endAt))
                          .routineOffsetDays(first.get(schedule.routineOffsetDays))
                          .alarmOffsetType(first.get(schedule.alarmOffsetType))
                          .recurrenceRule(first.get(schedule.recurrenceRule))
                          .scheduleOverrides(children)
                          .build();
                    }
                )
            ),
            m -> new ArrayList<>(m.values())
        ));
  }

  private BooleanBuilder buildWhereClause(QSchedule s, GetScheduleCriteria criteria) {

    return eqUserId(s, criteria.userId())
        .and(s.deletedAt.isNull())
        .and(s.overrideInfo.overrideType.isNull())
        .and(recurringCriteria(s, criteria).or(singleCriteria(s, criteria)));
  }

  private BooleanBuilder recurringCriteria(QSchedule s, GetScheduleCriteria criteria) {

    return new BooleanBuilder(s.recurrenceRule.isNotNull())
        .and(startAtBeforeToAt(s, criteria.toAt()))
        .and(s.recurrenceRule.until.isNull()
            .or(untilAtAfterFromAt(s, criteria.fromAt())));
  }

  private BooleanBuilder singleCriteria(QSchedule s, GetScheduleCriteria criteria) {

    return new BooleanBuilder(s.recurrenceRule.isNull())
        .and(startAtBeforeToAt(s, criteria.toAt()))
        .and(endAtAfterFromAt(s, criteria.fromAt()));
  }

  private static BooleanBuilder startAtBeforeToAt(QSchedule s, LocalDateTime toAt) {
    return nullSafeBuilder(() -> s.period.startAt.before(toAt));
  }

  private static BooleanBuilder untilAtAfterFromAt(QSchedule s, LocalDateTime fromAt) {
    return nullSafeBuilder(() -> s.recurrenceRule.until.after(fromAt));
  }

  private static BooleanBuilder endAtAfterFromAt(QSchedule s, LocalDateTime fromAt) {
    return nullSafeBuilder(() -> s.period.endAt.after(fromAt));
  }

  private BooleanBuilder eqUserId(QSchedule s, Long userId)  {
    return nullSafeBuilder(() -> s.userId.eq(userId));
  }
}
