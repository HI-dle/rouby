package com.rouby.common.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceAspect {

  @Around("execution(* com.rouby.schedule.application.service..*.*(..)) || " +
      "execution(* com.rouby.assistant.prompt.application.service..*.*(..)) ||" +
      "execution(* com.rouby.assistant.prompt.infrastructure.ai..*.*(..))")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    long end = System.currentTimeMillis();
    String methodName = joinPoint.getSignature().toShortString();
    System.out.println(methodName + " executed in " + (end - start) + "ms");

    return result;
  }
}
