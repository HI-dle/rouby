package com.rouby.routine.presentaion;

import com.rouby.routine.application.facade.RoutineFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/routine")
@RequiredArgsConstructor
public class RoutineController {

  private final RoutineFacade routineFacade;

}
