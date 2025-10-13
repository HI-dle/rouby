package com.rouby.assistant.feedback.application.port.outbound;

import com.rouby.assistant.feedback.application.dto.InfoForFeedback;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface AssistantGateway {

  <R> CompletableFuture<Void> requestDailyFeedbackAsync(
      int version, double temperature, InfoForFeedback command, Class<R> responseClazz,
      Consumer<R> onSuccess, Consumer<Throwable> onFailure);
}
