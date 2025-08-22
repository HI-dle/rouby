package com.rouby.user.device.domain.repository;

import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.device.domain.entity.vo.DeviceInfo;
import com.rouby.user.device.domain.entity.vo.DeviceTokenInfo;
import java.time.LocalDateTime;
import java.util.Optional;

public interface UserDeviceRepository {

  Optional<UserDevice> findByUserIdAndTokenInfo_DeviceToken(Long userId, String deviceToken);

  UserDevice save(UserDevice userDevice);

  int deleteByLastActiveAtBefore(LocalDateTime threshold);

  UserDevice saveAndFlush(UserDevice userDevice);

  int upsertUserDevice(Long userId, DeviceTokenInfo tokenInfo, DeviceInfo info);
}
