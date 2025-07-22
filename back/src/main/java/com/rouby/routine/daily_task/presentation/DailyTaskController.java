package com.rouby.routine.daily_task.presentation;

import com.rouby.routine.daily_task.application.facade.DailyTaskFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/daily-task")
@RequiredArgsConstructor
public class DailyTaskController {

  private final DailyTaskFacade dailyTaskFacade;

}
