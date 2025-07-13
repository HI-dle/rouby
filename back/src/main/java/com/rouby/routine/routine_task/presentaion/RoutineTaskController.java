package com.rouby.routine.routine_task.presentaion;

import com.rouby.routine.routine_task.application.facade.RoutineTaskFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routine")
@RequiredArgsConstructor
public class RoutineTaskController {

  private final RoutineTaskFacade routineTaskFacade;

}
