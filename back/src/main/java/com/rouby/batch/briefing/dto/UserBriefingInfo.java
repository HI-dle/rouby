package com.rouby.batch.briefing.dto;

import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.user.application.dto.info.UserInfo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record UserBriefingInfo(
    UserInfo userInfo,
    List<UserDevice> devices,
    LocalDate today,
    LocalTime briefingTime
) {

}
