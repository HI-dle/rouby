package com.rouby.routine.routine_task.presentaion;

import com.rouby.routine.routine_task.application.facade.RoutineTaskFacade;
import com.rouby.routine.routine_task.presentaion.dto.request.CreateRoutineTaskRequest;
import com.rouby.routine.routine_task.presentaion.dto.request.GetRoutineTaskRequest;
import com.rouby.routine.routine_task.presentaion.dto.response.GetRoutineTaskResponse;
import com.rouby.user.infrastructure.security.dto.SecurityUser;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/routine-task")
@RequiredArgsConstructor
public class RoutineTaskController {

  private final RoutineTaskFacade routineTaskFacade;

  @PreAuthorize("hasAnyRole('USER')")
  @PostMapping
  public ResponseEntity<Void> createRoutineTask(
      @AuthenticationPrincipal SecurityUser userDetails,
      @RequestBody @Validated CreateRoutineTaskRequest req) {
    Long routineTaskId = routineTaskFacade.createRoutineTask(req.toCommand(userDetails.getId()));
    URI location = ServletUriComponentsBuilder
        .fromCurrentRequestUri()
        .path("/{routineTaskId}")
        .buildAndExpand(routineTaskId)
        .toUri();
    return ResponseEntity.created(location).build();
  }


  @PreAuthorize("hasAnyRole('USER')")
  @GetMapping
  public ResponseEntity<GetRoutineTaskResponse> getRoutine(
      @AuthenticationPrincipal SecurityUser securityUser,
      @Valid @ModelAttribute GetRoutineTaskRequest getRoutineTaskRequest
  ) {
    return ResponseEntity.ok(GetRoutineTaskResponse.of(
        routineTaskFacade.getRoutineTask(
            getRoutineTaskRequest.toCommand(securityUser.getId()))));
  }

}
