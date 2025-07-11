package com.rouby.user.presentation;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
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
import com.rouby.user.presentation.dto.requset.FindPasswordRequest;
import com.rouby.user.presentation.dto.requset.ResetPasswordRequest;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerTestSupport {

  @BeforeEach
  void setUp() {
  }

  @WithMockCustomUser
  @DisplayName("비밀번호 찾기 API")
  @Test
  void findPassword() throws Exception {
    //given
    FindPasswordRequest request = FindPasswordRequest.builder().email("test@email.com").build();

    doNothing().when(userFacade).findPassword(argThat(req ->
        "test@email.com".equals(request.email())));

    //when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/users/password/find")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("find-password-user-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("유저 이메일"))
        ));
  }

  @WithMockCustomUser
  @DisplayName("비밀번호 변경 토큰 사용 API")
  @Test
  void resetPasswordByToken() throws Exception {
    //given
    ResetPasswordRequest request = ResetPasswordRequest.builder()
        .newPassword("newPassword")
        .token(UUID.randomUUID().toString())
        .build();

    String token = UUID.randomUUID().toString();

    doNothing().when(userFacade)
        .resetPasswordByToken(argThat(req ->
            "currentPassword".equals(request.newPassword())
                && "token".equals(request.token())));

    //when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/password/reset/token?token=" + token)
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("reset-password-by-token-user-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("token").description("비밀번호 변경 토큰"),
                fieldWithPath("newPassword").description("새 비밀번호"))
        ));
  }
}