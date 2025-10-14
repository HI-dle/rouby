package com.rouby.assistant.feedback.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.rouby.assistant.feedback.application.dto.CreateFeedbackCommand;
import com.rouby.assistant.feedback.application.dto.CreateFeedbackResult;
import com.rouby.assistant.feedback.application.dto.InfoForFeedback;
import com.rouby.assistant.feedback.application.exception.FeedbackErrorCode;
import com.rouby.assistant.feedback.application.port.outbound.AssistantGateway;
import com.rouby.assistant.feedback.application.usecase.CreateFeedbackUsecase;
import com.rouby.common.exception.CustomException;
import com.rouby.common.support.IntegrationTestSupport;
import com.rouby.user.user.domain.entity.User;
import com.rouby.user.user.fixture.UserFixture;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

class CreateFeedbackUsecaseIntegrationTest extends IntegrationTestSupport {

  @Autowired
  private CreateFeedbackUsecase createFeedbackUsecase;

  @MockitoBean
  private AssistantGateway assistantGateway;

  private User user;

  @BeforeEach
  void setUp() {
    user = UserFixture.create(1);
    userRepository.save(user);
  }

  @DisplayName("피드백 요청 생성 동시성 확인 - 두 요청 중 하나만 성공")
  @RepeatedTest(3)
  void createFeedbackRequestWithinQuota_concurrently_onlyOneSucceeds(RepetitionInfo info) {

    // given
    var command = CreateFeedbackCommand.builder()
        .userId(user.getId())
        .userInput("오늘의 피드백을 요청합니다!")
        .userMood("GOOD")
        .build();

    given(assistantGateway.requestDailyFeedbackAsync(
        any(Integer.class), any(Double.class), any(InfoForFeedback.class), any()))
        .willReturn(CompletableFuture.completedFuture(CreateFeedbackResult.builder().build()));

    int requestCnt = 2;
    CountDownLatch startGate = new CountDownLatch(1);
    Executor executor = Executors.newFixedThreadPool(requestCnt);

    Runnable runnable = () -> {
      try {
        startGate.await();
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      createFeedbackUsecase.requestFeedback(command);
    };

    List<CompletableFuture<Void>> futures = IntStream.range(0, requestCnt)
        .mapToObj(i -> CompletableFuture.runAsync(() -> {
          try {
            runnable.run();
          } catch (Exception e) {
            throw new CompletionException(e);
          }
        }, executor))
        .toList();

    // when
    startGate.countDown();
    List<Boolean> successResults = new ArrayList<>();
    List<Throwable> thrown = new ArrayList<>();

    for (CompletableFuture<Void> future : futures) {
      try {
        future.join();
        successResults.add(Boolean.TRUE);
      } catch (Exception e) {
        thrown.add(e.getCause());
      }
    }

    // then
    assertThat(successResults.size()).isEqualTo(1);
    assertThat(thrown.size()).isEqualTo(1);

    Throwable ex = thrown.get(0);
    if (ex instanceof CustomException ce) {
      assertThat(ce.getCode()).isEqualTo(FeedbackErrorCode.CONCURRENT_REQUEST_FAILED.getCode());
    } else {
      fail("예외가 예상한 CustomApiException이 아님: " + ex);
    }
  }
}