package com.rouby.user.user.presentation;

import static com.rouby.user.user.application.exception.UserErrorCode.INVALID_USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.support.ControllerTestSupport;
import com.rouby.user.user.application.dto.command.LoginCommand;
import com.rouby.user.user.application.dto.info.LoginInfo;
import com.rouby.user.user.application.exception.UserException;
import com.rouby.user.user.presentation.dto.request.LoginRequest;
import com.rouby.user.user.presentation.dto.request.RefreshTokenRequest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends ControllerTestSupport {


  @Test
  @DisplayName("로그인시 AccessToken과 RefreshToken을 반환한다.")
  void login_test() throws Exception {

    //given
    LoginRequest loginRequest = LoginRequest.builder()
        .email("test@gmail.com")
        .password("1234")
        .build();

    LoginCommand command = loginRequest.toApplication();
    String fakeToken = "Bearer fake.jwt.token";
    String refreshToken = UUID.randomUUID().toString();

    when(userUsecase.login(command)).thenReturn(new LoginInfo(fakeToken, refreshToken));

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)));

    // then
    result.andExpect(status().isOk())
        .andDo(document("login-auth-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("로그인 이메일"),
                fieldWithPath("password").description("로그인 비밀번호")
            )
        ));
  }

  @Test
  @DisplayName("로그인 실패")
  void login_fail_test() throws Exception {

    //given
    LoginRequest loginRequest = LoginRequest.builder()
        .email("jinyoungchoi")
        .password("1234")
        .build();

    doThrow(UserException.from(INVALID_USER)).when(userUsecase).login(any(LoginCommand.class));


    // when
    ResultActions result = mockMvc.perform(post("/api/v1/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(loginRequest)));

    // then
    result.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("login-email-invalid-401",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("로그인 이메일"),
                fieldWithPath("password").description("로그인 비밀번호")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("리프레시 요청 성공 API")
  void refresh_test() throws Exception {
    // given
    String fakeAccessToken = "fake.new.access.token";
    String fakeRefreshToken = UUID.randomUUID().toString();

    RefreshTokenRequest request = new RefreshTokenRequest("expired.access.token",
        "old.refresh.token");

    when(userUsecase.refresh(any())).thenReturn(
        new com.rouby.user.user.application.dto.info.TokenInfo(fakeAccessToken, fakeRefreshToken)
    );

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/auth/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    result.andExpect(status().isOk())
        .andDo(document("refresh-token-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("accessToken").description("만료된 엑세스 토큰"),
                fieldWithPath("refreshToken").description("갱신할 리프레시 토큰")
            )
        ));
  }
}
