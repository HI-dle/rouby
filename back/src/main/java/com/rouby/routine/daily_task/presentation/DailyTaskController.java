package com.rouby.routine.daily_task.presentation;

import com.rouby.routine.daily_task.application.facade.DailyTaskFacade;
import com.rouby.routine.daily_task.presentation.dto.ProgressDailyTaskRequest;
import com.rouby.user.infrastructure.security.dto.SecurityUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/daily-task")
@RequiredArgsConstructor
public class DailyTaskController {

  private final DailyTaskFacade dailyTaskFacade;

  @PostMapping("/progress")
  @PreAuthorize("hasAnyRole('USER')")
  public ResponseEntity<Void> progressDailyTask(
      @AuthenticationPrincipal SecurityUser securityUser,
      @RequestBody @Valid ProgressDailyTaskRequest request) {

    dailyTaskFacade.progressDailyTask(request.toCommand(securityUser.getId()));
    return ResponseEntity.ok().build();
  }
}
