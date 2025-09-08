package com.rouby.user.device.application.dto.command;

import java.util.List;

public record GetUserDeviceQuery(
    List<Long> userIdList
) {

}
