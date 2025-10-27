package com.rouby.user.device.application.service;

import com.rouby.user.device.application.dto.UserDeviceInfo;
import com.rouby.user.device.application.dto.command.GetUserDeviceQuery;
import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.device.domain.repository.UserDeviceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDeviceReadService {

  private final UserDeviceRepository userDeviceRepository;

  public List<UserDevice> findByUserIds(GetUserDeviceQuery query) {
    return userDeviceRepository.findByUserIdIn(query.userIdList());
  }

  public List<UserDeviceInfo> findByUserId(Long userId) {

    return userDeviceRepository.findByUserId(userId).stream()
        .map(UserDeviceInfo::from)
        .toList();
  }
}
