package com.rouby.batch.job.briefing.dto;

import com.rouby.user.device.domain.entity.UserDevice;
import com.rouby.user.user.application.dto.info.UserInfo;
import java.time.LocalDate;
import java.util.List;

public record UserBriefingInfo(
    UserInfo userInfo,
    List<UserDevice> devices,
    LocalDate today
) {

}
