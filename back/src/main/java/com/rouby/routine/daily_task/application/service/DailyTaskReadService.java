package com.rouby.routine.daily_task.application.service;

import com.rouby.routine.daily_task.domain.repository.DailyTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DailyTaskReadService {

  private final DailyTaskRepository dailyTaskRepository;

}
