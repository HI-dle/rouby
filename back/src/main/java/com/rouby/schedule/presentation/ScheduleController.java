package com.rouby.schedule.presentation;

import com.rouby.schedule.application.facade.ScheduleFacade;
import com.rouby.schedule.presentation.dto.DeleteScheduleFromRequest;
import com.rouby.schedule.presentation.dto.DeleteScheduleRequest;
import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest;
import com.rouby.schedule.presentation.dto.request.GetScheduleRequest;
import com.rouby.schedule.presentation.dto.request.UpdateScheduleRequest;
import com.rouby.schedule.presentation.dto.response.SchedulesResponse;
import com.rouby.user.user.infrastructure.security.dto.SecurityUser;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @PreAuthorize("hasAnyRole('USER')")
  @PutMapping
  public ResponseEntity<Long> updateSchedule(
      @AuthenticationPrincipal SecurityUser securityUser, @RequestBody @Validated UpdateScheduleRequest req) {

    Long scheduleId = scheduleFacade.updateSchedule(securityUser.getId(), req.toCommand());
    return ResponseEntity.ok().body(scheduleId);
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PatchMapping
  public ResponseEntity<Void> deleteSchedule(
      @AuthenticationPrincipal SecurityUser securityUser, @RequestBody @Validated DeleteScheduleRequest req) {

    scheduleFacade.deleteSchedule(securityUser.getId(), req.toCommand());
    return ResponseEntity.noContent().build();
  }

  @PreAuthorize("hasAnyRole('USER')")
  @PatchMapping("/from")
  public ResponseEntity<Void> deleteSchedulesStartingFrom(
      @AuthenticationPrincipal SecurityUser securityUser, @RequestBody @Validated DeleteScheduleFromRequest req) {

    scheduleFacade.deleteSchedulesStartingFrom(securityUser.getId(), req.toCommand());
    return ResponseEntity.noContent().build();
  }
}
