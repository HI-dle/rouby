package com.rouby.user.device.application.service;

import com.rouby.user.device.application.dto.command.RegisterUserDeviceCommand;
import com.rouby.user.device.domain.repository.UserDeviceRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

  public int deleteStaleDeviceTokens(Period staleThreshold) {
    return userDeviceRepository.deleteByLastActiveAtBefore(LocalDate.now().minus(staleThreshold));
  }
}
