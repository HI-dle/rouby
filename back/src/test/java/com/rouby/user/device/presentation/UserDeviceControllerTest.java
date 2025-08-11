package com.rouby.user.device.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.user.device.fixture.UserDeviceFixture;
import com.rouby.user.device.presentation.dto.request.RegisterUserDeviceRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserDeviceControllerTest extends ControllerTestSupport {

  @WithMockCustomUser
  @Test
  @DisplayName("회원 디바이스 정보 등록 API - 성공")
  void registerUserDevice() throws Exception {
    RegisterUserDeviceRequest request = UserDeviceFixture.getSuccessRequest();
    String content = objectMapper.writeValueAsString(request);

    doNothing().when(userDeviceFacade).register(any());

    ResultActions resultActions = mockMvc.perform(post("/api/v1/users/devices")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("register-user-device-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("deviceToken").description("기기 토큰"),
                fieldWithPath("tokenProvider").description("토큰 제공자"),
                fieldWithPath("appType").description("어플리케이션 타입"),
                fieldWithPath("appVersion").description("어플리케이션 버전"),
                fieldWithPath("deviceType").description("기기 종류"),
                fieldWithPath("os").description("운영체제 정보"),
                fieldWithPath("browser").description("브라우저 정보"),
                fieldWithPath("userAgent").description("사용자 접속 정보")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("회원 디바이스 정보 등록 API - 유효하지 못한 요청으로 실패 400")
  @Test
  void registerUserDevice_failed_request() throws Exception {

    // given
    RegisterUserDeviceRequest request = UserDeviceFixture.getFailedRequest();
    String content = objectMapper.writeValueAsString(request);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/users/devices")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("register-user-device-invalid-request-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getValidationErrorResponseFieldSnippet()
        ));
  }
}