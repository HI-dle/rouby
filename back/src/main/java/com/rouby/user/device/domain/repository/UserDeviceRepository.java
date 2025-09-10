package com.rouby.user.device.domain.repository;

import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.device.domain.entity.enums.TokenProviderType;
import com.rouby.user.device.domain.entity.vo.DeviceInfo;
import com.rouby.user.device.domain.entity.vo.DeviceTokenInfo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserDeviceRepository {

  Optional<UserDevice> findByUserIdAndTokenInfo_DeviceToken(Long userId, String deviceToken);

  UserDevice save(UserDevice userDevice);

  int deleteByLastActiveAtBefore(LocalDateTime threshold);

  UserDevice saveAndFlush(UserDevice userDevice);

  int upsertUserDevice(Long userId, DeviceTokenInfo tokenInfo, DeviceInfo info);

  int deleteByUserIdAndTokenInfo_deviceTokenAndTokenInfo_tokenProvider(
      Long userId, String deviceToken, TokenProviderType tokenProvider);

  int deleteAllByUserId(Long userId);

  List<UserDevice> findByUserIdIn(List<Long> userIds);
}
