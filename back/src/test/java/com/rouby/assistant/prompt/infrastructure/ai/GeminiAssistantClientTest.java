package com.rouby.assistant.prompt.infrastructure.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rouby.assistant.feedback.application.dto.CreateFeedbackResult;
import com.rouby.assistant.prompt.domain.info.AssistantResponse;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantErrorCode;
import com.rouby.assistant.prompt.infrastructure.exception.AssistantInfraException;
import com.rouby.common.exception.CustomException;
import com.rouby.common.support.IntegrationTestSupport;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.State;
import io.github.resilience4j.retry.RetryRegistry;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ResponseEntity;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


class GeminiAssistantClientTest extends IntegrationTestSupport {

  @MockitoBean(answers = Answers.RETURNS_DEEP_STUBS)
  ChatClient chatClient;

  @Autowired
  ResponseConverterManager responseConverterManager;

  @Autowired
  GeminiAssistantClient client;

  private CircuitBreaker cb;

  @Autowired
  RetryRegistry retryRegistry;

  @BeforeEach
  void setUp() {
    this.cb = cbRegistry.circuitBreaker("assistant");
  }

  @Test
  @SuppressWarnings("unchecked")
  void checkRetryWhenGenerateFeedbackResponseFromPrompt() {
    cb.transitionToClosedState();
    // given
    CreateFeedbackResult feedbackResult = CreateFeedbackResult.builder()
        .feedback("SUCCESS")
        .build();
    BeanOutputConverter<CreateFeedbackResult> converter =
        responseConverterManager.getConverter(CreateFeedbackResult.class);
    ResponseEntity<ChatResponse, CreateFeedbackResult> responseEntity =
        new ResponseEntity<> (new ChatResponse(Collections.emptyList()), feedbackResult);

    // when
    CallResponseSpec mockResult = Mockito.mock(CallResponseSpec.class);
    when(mockResult.responseEntity(converter)).thenReturn(responseEntity);

    AtomicInteger tries = new AtomicInteger();
    when(chatClient.prompt()
        .system(anyString())
        .options(any())
        .user(any(Consumer.class))
        .call())
        .thenAnswer(inv -> {
          int cnt = tries.incrementAndGet();
          if (cnt < 3) throw new RuntimeException();
          return mockResult;
        });

    AssistantResponse<CreateFeedbackResult> resp = client.generateResponseFromPrompt(
        0.2, "sys", "user",
        Map.of("k","v"), CreateFeedbackResult.class);

    // then
    assertThat(resp.result().feedback()).isEqualTo("SUCCESS");
    assertThat(tries.get()).isEqualTo(3);
  }

  @Test
  @SuppressWarnings("unchecked")
  void fallbackAndCircuitBreakerOpen() {

    cb.transitionToClosedState();
    CircuitBreaker.Metrics metrics = cb.getMetrics();
    verify(chatClient, times(0)).prompt();

    // 항상 실패
    when(chatClient.prompt()
        .system(anyString())
        .options(any())
        .user(any(Consumer.class))
        .call())
        .thenThrow(AssistantInfraException.class)
        .thenThrow(new RuntimeException("always"));
    verify(chatClient, times(1)).prompt(); // when 절에서 한 번 호출

    // 무시되는 예외
    assertThatThrownBy(() ->
        client.generateResponseFromPrompt(
            0.2, "s", "u", Map.of(), String.class))
        .isInstanceOf(AssistantInfraException.class);

    // 1) 첫 호출: 실패 ->  RuntimeException 발생, CB는 아직 CLOSED(최소 호출수 미만)
    assertThatThrownBy(() ->
        client.generateResponseFromPrompt(
            0.2, "s", "u", Map.of(), String.class))
        .isInstanceOf(RuntimeException.class);

    // 2) 두 번째 호출: 또 실패 -> 실패율 100%, min calls(=6) 도달 -> CB OPEN
    assertThatThrownBy(() ->
        client.generateResponseFromPrompt(
            0.2, "s", "u", Map.of(), String.class))
        .isInstanceOf(RuntimeException.class);

    verify(chatClient, times(8)).prompt();
    assertThat(metrics.getNumberOfFailedCalls()).isEqualTo(6);
    assertThat(cb.getState()).isEqualTo(State.OPEN);

    // 3) 세 번째 호출: OPEN 상태이므로 외부 호출 없이 즉시 fallback 경로로 단락
    //    => chatClient 가 전혀 호출되지 않아야 함
    Mockito.clearInvocations(chatClient); // 호출 카운터 초기화
    assertThatThrownBy(() ->
        client.generateResponseFromPrompt(
            0.2,"s","u", Map.of(), String.class))
        .isInstanceOf(AssistantInfraException.class)
        .hasMessage(AssistantErrorCode.SERVICE_UNAVAILABLE.getMessage());

    // prompt() 자체가 호출되지 않았는지 확인
    verify(chatClient, never()).prompt();
  }
}