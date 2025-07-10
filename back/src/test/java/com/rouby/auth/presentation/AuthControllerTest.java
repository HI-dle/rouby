package com.rouby.auth.presentation;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.auth.application.dto.request.LoginCommand;
import com.rouby.auth.application.dto.response.LoginInfo;
import com.rouby.auth.presentation.dto.request.LoginRequest;
import com.rouby.common.support.ControllerTestSupport;
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
  @DisplayName("로그인시 AccessToken을 반환한다.")
  void login_test() throws Exception {

    //given

    LoginRequest loginRequest = LoginRequest.builder()
        .email("test@gmail.com")
        .password("1234")
        .build();

    LoginCommand command = loginRequest.toApplication();
    String fakeToken = "Bearer fake.jwt.token";

    when(authFacade.login(command)).thenReturn(new LoginInfo(fakeToken));

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

}
