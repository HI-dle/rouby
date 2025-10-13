package com.rouby.assistant.feedback.presentation;

import com.rouby.assistant.feedback.application.usecase.CreateFeedbackUsecase;
import com.rouby.assistant.feedback.presentation.dto.CreateFeedbackRequest;
import com.rouby.user.user.infrastructure.security.dto.SecurityUser;
import java.net.URI;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/assistants/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

  private final CreateFeedbackUsecase createFeedbackUsecase;

  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping
  public ResponseEntity<Void> createDailyFeedback(
      @AuthenticationPrincipal SecurityUser user,
      @RequestBody @Validated CreateFeedbackRequest request) {

    createFeedbackUsecase.requestFeedback(request.toCommand(user.getId()));
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{date}")
        .buildAndExpand(LocalDate.now())
        .toUri();
    return ResponseEntity.created(location).build();
  }
}
