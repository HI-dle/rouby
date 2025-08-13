package com.rouby.user.device.application.service;

import com.rouby.user.device.application.dto.command.RegisterUserDeviceCommand;
import com.rouby.user.device.domain.repository.UserDeviceRepository;
import java.time.LocalDateTime;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeviceWriteService {

  private final UserDeviceRepository userDeviceRepository;

  @Transactional
  public void register(RegisterUserDeviceCommand command) {

    userDeviceRepository.upsertUserDevice(
        command.userId(),
        command.buildDeviceTokenInfo(),
        command.buildDeviceInfo());
  }

  @Transactional
  public int deleteStaleDeviceTokens(Period staleThreshold) {
    LocalDateTime threshold = LocalDateTime.now().minus(staleThreshold);
    return userDeviceRepository.deleteByLastActiveAtBefore(threshold);
  }
}
