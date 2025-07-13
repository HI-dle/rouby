package com.rouby.assistant.recommendation.infrastructure.ai.test.dto;

/**
 * @Date : 2025. 07. 11.
 *
 * @author : hanjihoon
 */
public record TestResponse(boolean result,
                           boolean hasTime,
                           String datetime,
                           String content) {}
