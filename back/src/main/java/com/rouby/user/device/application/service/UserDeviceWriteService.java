package com.rouby.user.device.application.service;

import com.rouby.common.exception.CustomException;
import com.rouby.user.device.application.dto.command.DeleteUserDeviceCommand;
import com.rouby.user.device.application.dto.command.RegisterUserDeviceCommand;
import com.rouby.user.device.application.exception.UserDeviceErrorCode;
import com.rouby.user.device.domain.entity.enums.TokenProviderType;
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
  public int hardDeleteStaleDeviceTokens(Period staleThreshold) {

    LocalDateTime threshold = LocalDateTime.now().minus(staleThreshold);
    return userDeviceRepository.deleteByLastActiveAtBefore(threshold);
  }

  @Transactional
  public void hardDelete(DeleteUserDeviceCommand command) {

    int result = userDeviceRepository.deleteByUserIdAndTokenInfo_deviceTokenAndTokenInfo_tokenProvider(
        command.userId(), command.deviceToken(), TokenProviderType.parse(command.tokenProvider()));
    if (result < 1) throw CustomException.from(UserDeviceErrorCode.NOT_FOUND_USER_DEVICE);
  }

  @Transactional
  public void hardDeleteAllByUser(Long userId) {

    userDeviceRepository.deleteAllByUserId(userId);
  }
}
