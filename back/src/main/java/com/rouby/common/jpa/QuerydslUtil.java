package com.rouby.common.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.function.Supplier;

public class QuerydslUtil {

  private QuerydslUtil() {
    throw new UnsupportedOperationException("Utility Class");
  }

  public static BooleanBuilder nullSafeBuilder(Supplier<BooleanExpression> f) {
    try {
      BooleanExpression expression = f.get();
      return expression != null
          ? new BooleanBuilder(expression)
          : new BooleanBuilder();
    } catch (RuntimeException e) {
      return new BooleanBuilder();
    }
  }
}
