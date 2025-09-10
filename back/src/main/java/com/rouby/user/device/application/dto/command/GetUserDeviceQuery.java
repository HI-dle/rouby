package com.rouby.user.device.application.dto.command;

import static com.rouby.user.device.application.exception.UserDeviceErrorCode.USER_ID_LIST_EMPTY;

import com.rouby.common.exception.CustomException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

public record GetUserDeviceQuery(
    List<Long> userIdList
) {

  public GetUserDeviceQuery {
    Objects.requireNonNull(userIdList, "userIdList");
    if (userIdList.isEmpty()) {
      throw CustomException.from(USER_ID_LIST_EMPTY);
    }
    userIdList = List.copyOf(new LinkedHashSet<>(userIdList));
  }
}
