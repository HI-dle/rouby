package com.rouby.user.presentation.dto.request;

import com.rouby.user.application.dto.command.UpdateUserInfoCommand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import java.util.Set;

public record UpdateMyUserInfoRequest(
    @NotBlank(message = "nickname은 필수 값입니다.")
    @Size(max = 20, message = "nickname은 최대 20자까지 입력할 수 있습니다.")
    String nickname,

    @NotNull(message = "healthStatusKeywords는 필수 값입니다.")
    @Size(max = 20, message = "healthStatusKeywords는 최대 20개까지 입력할 수 있습니다.")
    Set<String> healthStatusKeywords,

    @NotNull(message = "profileKeywords는 필수 값입니다.")
    @Size(max = 20, message = "profileKeywords는 최대 20개까지 입력할 수 있습니다.")
    Set<String> profileKeywords,

    @NotNull(message = "dailyStartTime는 필수 값입니다.")
    LocalTime dailyStartTime,

    @NotNull(message = "dailyEndTime는 필수 값입니다.")
    LocalTime dailyEndTime
    ) {

    public UpdateUserInfoCommand toCommand(Long updaterId) {
        return UpdateUserInfoCommand.builder()
            .updaterId(updaterId)
            .nickname(nickname)
            .healthStatusKeywords(healthStatusKeywords)
            .profileKeywords(profileKeywords)
            .dailyStartTime(dailyStartTime)
            .dailyEndTime(dailyEndTime)
            .build();
    }
}
