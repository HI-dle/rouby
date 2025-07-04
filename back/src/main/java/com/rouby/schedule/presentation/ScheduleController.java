package com.rouby.schedule.presentation;

import com.rouby.schedule.application.facade.ScheduleFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

  private final ScheduleFacade scheduleFacade;


}
