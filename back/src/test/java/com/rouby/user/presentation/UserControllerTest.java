package com.rouby.user.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.user.presentation.dto.request.ResetPasswordRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerTestSupport {

  @WithMockCustomUser
  @DisplayName("비밀번호 변경 API")
  @Test
  void resetPassword() throws Exception {

    //given
    ResetPasswordRequest request = ResetPasswordRequest.builder()
        .currentPassword("currentPassword")
        .newPassword("newPassword")
        .build();

    Long userId = 1L;

    doNothing().when(userFacade).resetPassword(eq(userId), any());

    //when
    ResultActions resultActions = mockMvc.perform(patch("/api/v1/users/password/reset")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reset-password-user-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("currentPassword").description("현재 비밀번호"),
                fieldWithPath("newPassword").description("새 비밀번호"))
        ));
  }
}