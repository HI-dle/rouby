package com.rouby.user.device.application.facade;

import com.rouby.user.device.application.dto.command.DeleteUserDeviceCommand;
import com.rouby.user.device.application.dto.command.RegisterUserDeviceCommand;
import com.rouby.user.device.application.service.UserDeviceWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDeviceFacade {

  private final UserDeviceWriteService userDeviceWriteService;

  public void register(RegisterUserDeviceCommand command) {
    userDeviceWriteService.register(command);
  }

  public void hardDelete(DeleteUserDeviceCommand command) {
    userDeviceWriteService.hardDelete(command);
  }

  public void hardDeleteAllByUser(Long userId) {
    userDeviceWriteService.hardDeleteAllByUser(userId);
  }
}
