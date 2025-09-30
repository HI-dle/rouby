package com.rouby.routine.routine_task.infrastructure.persistence.jpa.impl;

import static com.rouby.common.jpa.QuerydslUtil.nullSafeBuilder;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rouby.routine.routine_task.domain.QRoutineTask;
import com.rouby.routine.routine_task.domain.repository.search.GetRoutineTaskCriteria;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskOverride;
import com.rouby.routine.routine_task.domain.repository.search.RoutineTaskWithOverrides;
import com.rouby.routine.routine_task.infrastructure.persistence.jpa.RoutineTaskJpaRepositoryCustom;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class RoutineTaskJpaRepositoryCustomImpl implements RoutineTaskJpaRepositoryCustom {

  private final JPAQueryFactory queryFactory;
  private final QRoutineTask parent = QRoutineTask.routineTask;
  private final QRoutineTask child = new QRoutineTask("child");

  @Override
  public List<RoutineTaskWithOverrides> findRoutineTaskByCriteria(GetRoutineTaskCriteria criteria) {

    List<Tuple> tuples = queryFactory
        .select(
            parent.id,
            parent.userId,
            parent.title,
            parent.taskType,
            parent.routineTimeInfo,
            parent.recurrenceRule,
            parent.alarmOffsetType,
            parent.targetValue,
            child.id,
            child.title,
            child.routineTimeInfo,
            child.targetValue,
            child.overrideInfo.overrideType,
            child.overrideInfo.overrideDate
        )
        .from(parent)
        .leftJoin(child)
        .on(
            parent.recurrenceRule.isNotNull()
                .and(child.deletedAt.isNull())
                .and(child.parentRoutineTask.eq(parent))
                .and(child.routineTimeInfo.time.isNotNull())
        )
        .where(buildWhereClause(parent, criteria))
        .orderBy(parent.userId.asc(), parent.routineTimeInfo.startDate.asc())
        .fetch();

    return convertGroupedList(tuples);
  }

  private BooleanBuilder buildWhereClause(QRoutineTask rt, GetRoutineTaskCriteria criteria) {
    return eqUserId(rt, criteria.userId())
        .and(rt.deletedAt.isNull())
        .and(rt.overrideInfo.overrideType.isNull())
        .and(recurringCriteria(rt, criteria).or(singleCriteria(rt, criteria)));
  }

  private BooleanBuilder recurringCriteria(QRoutineTask rt, GetRoutineTaskCriteria criteria) {
    return new BooleanBuilder(rt.recurrenceRule.isNotNull())
        .and(rt.routineTimeInfo.startDate.loe(criteria.toDate()))
        .and(rt.recurrenceRule.until.isNull()
            .or(rt.recurrenceRule.until.goe(criteria.fromDate().atStartOfDay())));
  }

  private BooleanBuilder singleCriteria(QRoutineTask rt, GetRoutineTaskCriteria criteria) {
    return new BooleanBuilder(rt.recurrenceRule.isNull())
        .and(rt.routineTimeInfo.startDate.loe(criteria.toDate()))
        .and(rt.routineTimeInfo.untilDate.goe(criteria.fromDate()));
  }

  private BooleanBuilder eqUserId(QRoutineTask rt, Long userId) {
    return nullSafeBuilder(() -> rt.userId.eq(userId));
  }

  private List<RoutineTaskWithOverrides> convertGroupedList(List<Tuple> tuples) {
    return tuples.stream()
        .collect(Collectors.collectingAndThen(
            Collectors.groupingBy(
                t -> t.get(parent.id),
                LinkedHashMap::new,
                Collectors.toList()
            ),
            grouped -> grouped.values().stream().map(rows -> {
              Tuple main = rows.get(0);

              List<RoutineTaskOverride> overrides = rows.stream()
                  .filter(t -> t.get(child.id) != null)
                  .map(t -> new RoutineTaskOverride(
                      t.get(child.id),
                      t.get(child.title),
                      t.get(child.targetValue),
                      t.get(child.routineTimeInfo),
                      t.get(child.overrideInfo.overrideType),
                      t.get(child.overrideInfo.overrideDate)
                  ))
                  .sorted(Comparator.comparing(RoutineTaskOverride::overrideDate))
                  .toList();

              return new RoutineTaskWithOverrides(
                  main.get(parent.id),
                  main.get(parent.userId),
                  main.get(parent.title),
                  main.get(parent.taskType),
                  main.get(parent.targetValue),
                  main.get(parent.routineTimeInfo),
                  main.get(parent.recurrenceRule),
                  main.get(parent.alarmOffsetType),
                  overrides
              );
            }).toList()
        ));
  }
}
