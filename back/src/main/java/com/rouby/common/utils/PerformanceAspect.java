package com.rouby.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

  @Around("execution(* com.rouby.schedule.application.service..*.*(..)) || " +
      "execution(* com.rouby.assistant.prompt.application.service..*.*(..)) ||" +
      "execution(* com.rouby.assistant.prompt.infrastructure.ai..*.*(..)) ||" +
      "execution(* com.rouby.assistant.briefing..*.*(..))")
  public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();

    Object result = joinPoint.proceed();

    long end = System.currentTimeMillis();
    String methodName = joinPoint.getSignature().toShortString();
    log.info("{} executed in {}ms", methodName, (end - start));

    return result;
  }
}
