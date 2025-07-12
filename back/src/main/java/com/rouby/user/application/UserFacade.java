package com.rouby.user.application;

import com.rouby.user.application.dto.command.CreateUserCommand;
import com.rouby.user.application.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserWriteService userWriteService;

  public void createUser(CreateUserCommand command){
    userWriteService.create(command);
  }
}
