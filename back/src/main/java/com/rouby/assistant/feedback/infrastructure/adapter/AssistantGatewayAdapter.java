package com.rouby.assistant.feedback.infrastructure.adapter;

import static reactor.core.Exceptions.unwrap;

import com.rouby.assistant.feedback.application.dto.InfoForFeedback;
import com.rouby.assistant.feedback.application.port.outbound.AssistantGateway;
import com.rouby.assistant.prompt.application.usecase.CreateAssistantResponseUsecase;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssistantGatewayAdapter implements AssistantGateway {

  private final CreateAssistantResponseUsecase assistantUsecase;

  @Async("llmExecutor")
  @Override
  public <R> CompletableFuture<Void> requestDailyFeedbackAsync(
      int version, double temperature, InfoForFeedback command, Class<R> responseClazz,
      Consumer<R> onSuccess, Consumer<Throwable> onFailure) {

    return CompletableFuture.supplyAsync(() ->
        assistantUsecase.createAssistantResponse(
            "FEEDBACK", version, temperature, getModel(command), responseClazz)
    ).thenAccept(result -> {
      try {
        onSuccess.accept(result);
      } catch (Throwable t) {
        onFailure.accept(unwrap(t));
      }
    }).exceptionally(t -> {
      onFailure.accept(unwrap(t));
      return null;
    });
  }

  private Map<String, Object> getModel(InfoForFeedback command) {

    return Map.of("userNickname", command.nickname(),
        "userSchedule", command.schedulesInfo(),
        "userRoutine", command.routineTasksWithProgress().routines().subList(0, 10),
        "roubyTone", command.communicationTone(),
        "userMood", command.userMood(),
        "userInput", command.userInput(),
        "userProfileKeyword", command.profileKeywords(),
        "userStatusKeyword", command.healthStatusKeywords(),
        "roubyFeedbackKeyword", command.recentFeedbackKeywords(),
        "userRecentStatusKeyword", command.recentStatusKeywords());
  }
}
