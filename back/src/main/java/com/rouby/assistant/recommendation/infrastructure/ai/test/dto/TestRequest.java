package com.rouby.assistant.recommendation.infrastructure.ai.test.dto;

import java.time.LocalDate;

/**
 * @Date : 2025. 07. 11.
 *
 * @author : hanjihoon
 */
//일시적인 테스트용 입니다. 의존성 역행해도 이해 바랍니다.
public record TestRequest(LocalDate date,
                          String content) {}
