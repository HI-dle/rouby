package com.rouby.schedule.infrastructure.persistence.jpa;

import static com.rouby.common.jpa.QuerydslUtil.nullSafeBuilder;
import static com.rouby.schedule.domain.entity.QSchedule.schedule;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.schedule.domain.entity.QSchedule;
import com.rouby.schedule.domain.repository.criteria.GetScheduleCriteria;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides;
import com.rouby.schedule.domain.repository.info.ScheduleWithOverrides.ScheduleOverride;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScheduleJpaRepositoryCustomImpl implements ScheduleJpaRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;
  private final QSchedule child = new QSchedule("child");

  @Override
  public List<ScheduleWithOverrides> findSchedulesByCriteria(GetScheduleCriteria criteria) {

    List<Tuple> tuples = jpaQueryFactory.select(
        schedule.id,
        schedule.userId,
        schedule.title,
        schedule.memo,
        schedule.period.startAt,
        schedule.period.endAt,
        schedule.routineActivateDate,
        schedule.alarmOffsetType,
        schedule.recurrenceRule,
        child.id,
        child.userId,
        child.title,
        child.memo,
        child.period.startAt,
        child.period.endAt,
        child.alarmOffsetType,
        child.overrideInfo.overrideType,
        child.overrideInfo.overrideDate)
        .from(schedule)
        .leftJoin(child)
        .on(
            schedule.recurrenceRule.isNotNull()
            .and(child.parentSchedule.eq(schedule))
        )
        .where(buildWhereClause(criteria))
        .orderBy(schedule.period.startAt.asc())
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
                              .alarmOffsetType(t.get(child.alarmOffsetType))
                              .overrideType(t.get(child.overrideInfo.overrideType))
                              .overrideDate(t.get(child.overrideInfo.overrideDate))
                              .build()
                          )
                          .toList();

                      return ScheduleWithOverrides.builder()
                          .id(first.get(schedule.id))
                          .userId(first.get(schedule.userId))
                          .title(first.get(schedule.title))
                          .memo(first.get(schedule.memo))
                          .startAt(first.get(schedule.period.startAt))
                          .endAt(first.get(schedule.period.endAt))
                          .routineActivateDate(first.get(schedule.routineActivateDate))
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

  private BooleanBuilder buildWhereClause(GetScheduleCriteria criteria) {
    return eqUserId(criteria.userId())
        .and(schedule.deletedAt.isNull())
        .and(schedule.overrideInfo.overrideType.isNull())
        .and(recurringCriteria(criteria).or(singleCriteria(criteria)));
  }

  private BooleanBuilder recurringCriteria(GetScheduleCriteria criteria) {

    return new BooleanBuilder().and(schedule.recurrenceRule.isNotNull())
        .and(startAtBeforeToAt(criteria.toAt()))
        .and(schedule.recurrenceRule.until.isNull()
            .or(untilAtAfterFromAt(criteria.fromAt())));
  }

  private BooleanBuilder singleCriteria(GetScheduleCriteria criteria) {

    return new BooleanBuilder().and(schedule.recurrenceRule.isNull())
        .and(startAtBeforeToAt(criteria.toAt()))
        .and(endAtAfterFromAt(criteria.fromAt()));
  }

  private static BooleanBuilder startAtBeforeToAt(LocalDateTime toAt) {
    return nullSafeBuilder(() -> schedule.period.startAt.before(toAt));
  }

  private static BooleanBuilder untilAtAfterFromAt(LocalDateTime fromAt) {
    return nullSafeBuilder(() -> schedule.recurrenceRule.until.after(fromAt));
  }

  private static BooleanBuilder endAtAfterFromAt(LocalDateTime fromAt) {
    return nullSafeBuilder(() -> schedule.period.endAt.after(fromAt));
  }

  private BooleanBuilder eqUserId(Long userId)  {
    return nullSafeBuilder(() -> schedule.userId.eq(userId));
  }
}
