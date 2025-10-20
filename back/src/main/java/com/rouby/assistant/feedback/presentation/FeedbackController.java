package com.rouby.assistant.feedback.presentation;

import com.rouby.assistant.feedback.application.service.FeedbackReadService;
import com.rouby.assistant.feedback.application.usecase.CreateFeedbackUsecase;
import com.rouby.assistant.feedback.presentation.dto.CreateFeedbackRequest;
import com.rouby.assistant.feedback.presentation.dto.GetDailyFeedbacksResponse;
import com.rouby.user.user.infrastructure.security.dto.SecurityUser;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assistants/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

  private final CreateFeedbackUsecase createFeedbackUsecase;
  private final FeedbackReadService feedbackReadService;

  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping
  public ResponseEntity<Void> createDailyFeedback(
      @AuthenticationPrincipal SecurityUser user,
      @RequestBody @Validated CreateFeedbackRequest request) {

    createFeedbackUsecase.requestFeedback(request.toCommand(user.getId()));
    return ResponseEntity.accepted().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping("/daily/{date}")
  public ResponseEntity<GetDailyFeedbacksResponse> getDailyFeedback(
      @AuthenticationPrincipal SecurityUser user,
      @PathVariable LocalDate date) {

    GetDailyFeedbacksResponse res = GetDailyFeedbacksResponse.from(
        feedbackReadService.getDailyFeedbacks(user.getId(), date));
    return ResponseEntity.ok(res);
  }
}
