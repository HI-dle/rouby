package com.rouby.routine.daily_task.application.facade;

import com.rouby.routine.daily_task.application.service.DailyTaskReadService;
import com.rouby.routine.daily_task.application.service.DailyTaskWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyTaskFacade {

  private final DailyTaskReadService dailyTaskReadService;
  private final DailyTaskWriteService dailyTaskWriteService;

}
