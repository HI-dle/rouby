package com.rouby.user.device.application.service;

import com.rouby.user.device.application.dto.command.GetUserDeviceQuery;
import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.device.domain.repository.UserDeviceRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDeviceReadService {

  private final UserDeviceRepository userDeviceRepository;

  @Transactional(readOnly = true)
  public List<UserDevice> findByUserIds(GetUserDeviceQuery query) {
    return userDeviceRepository.findByUserIdIn(query.userIdList());
  }
}
