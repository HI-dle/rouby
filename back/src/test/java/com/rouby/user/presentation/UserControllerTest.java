package com.rouby.user.presentation;

import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.auth.config.SecurityConfig;
import com.rouby.common.config.WebConfig;
import com.rouby.common.exception.GlobalExceptionHandler;
import com.rouby.common.resolver.CustomPageableArgumentResolver;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.schedule.presentation.ScheduleController;
import com.rouby.user.application.UserFacade;
import com.rouby.user.presentation.dto.CreateUserRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@AutoConfigureRestDocs
@WebMvcTest(
    controllers = {UserController.class,},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE) //, classes = JwtAuthenticationFilter.class)
    }
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Import({
    WebConfig.class,
    CustomPageableArgumentResolver.class,
    GlobalExceptionHandler.class,
})
class UserControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockitoBean
  UserFacade userFacade;

  @Test
  @DisplayName("회원 가입 API")
  void createUser() throws Exception {

    // given
    CreateUserRequest request = UserRequestStub.valid();
    doNothing().when(userFacade).createUser(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-create",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소"),
                fieldWithPath("password").description("비밀번호 (8~32자, 2종류 이상 조합)")
            )
        ));
  }

  @Test
  @DisplayName("이메일 형식 오류 시 400 응답 문서화")
  void createUser_invalidEmail() throws Exception {
    CreateUserRequest request = UserRequestStub.withInvalidEmail();

    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(document("user-create-invalid-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소 (형식 오류)"),
                fieldWithPath("password").description("비밀번호")
            ),
            getErrorResponseFieldssnippet()
        ));
  }

  private static ResponseFieldsSnippet getErrorResponseFieldssnippet() {
    return responseFields(
        fieldWithPath("message").description("에러 메시지"),
        fieldWithPath("code").description("에러 코드"),
        fieldWithPath("errors[].value").description("유효하지 않은 필드명"),
        fieldWithPath("errors[].message").description("유효성 검증 실패 메시지")
    );
  }


  @Test
  @DisplayName("비밀번호 형식 오류 시 400 응답 문서화")
  void createUser_invalidPassword() throws Exception {
    CreateUserRequest request = UserRequestStub.withInvalidPassword();

    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(document("user-create-invalid-password",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소"),
                fieldWithPath("password").description("비밀번호 (형식 오류)")
            ),
            getErrorResponseFieldssnippet()
        ));
  }
}