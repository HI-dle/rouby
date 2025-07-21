package com.rouby.schedule.presentation;

import com.rouby.schedule.application.facade.ScheduleFacade;
import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest;
import com.rouby.schedule.presentation.dto.request.GetScheduleRequest;
import com.rouby.schedule.presentation.dto.response.SchedulesResponse;
import com.rouby.user.infrastructure.security.dto.SecurityUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/schedules")
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleFacade scheduleFacade;

  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping
  public ResponseEntity<Void> createSchedule(
      @AuthenticationPrincipal SecurityUser securityUser, @RequestBody @Validated CreateScheduleRequest req) {

    Long scheduleId = scheduleFacade.createSchedule(securityUser.getId(), req.toCommand());
    URI location =  ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{scheduleId}")
        .buildAndExpand(scheduleId)
        .toUri();
    return ResponseEntity.created(location).build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping
  public ResponseEntity<SchedulesResponse> getSchedules(
      @AuthenticationPrincipal SecurityUser securityUser, @Validated GetScheduleRequest req) {

    SchedulesResponse response =
        SchedulesResponse.of(scheduleFacade.getSchedules(req.toQuery(securityUser.getId())));

    return ResponseEntity.ok().body(response);
  }
}
