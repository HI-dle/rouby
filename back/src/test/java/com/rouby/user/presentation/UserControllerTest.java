package com.rouby.user.presentation;

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

import com.rouby.common.support.ControllerTestSupport;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.presentation.dto.CreateUserRequest;
import com.rouby.user.presentation.dto.SendEmailVerificationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerTestSupport {

  @Test
  @DisplayName("회원 가입 성공")
  void createUser() throws Exception {

    // given
    CreateUserRequest request = UserRequestStub.valid();
    doNothing().when(userFacade).createUser(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isCreated())
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
  @DisplayName("회원 가입 실패 - 이메일 형식 오류")
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
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("회원 가입 실패 - 비밀번호 형식 오류")
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
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("이메일 인증 코드 전송 성공")
  void requestEmail() throws Exception {

    // given
    SendEmailVerificationRequest request = UserRequestStub.toSendEmailVerificationRequest();
    doNothing().when(userFacade).sendEmailVerification(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/request")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-email-verification-request",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소")
            )
        ));
  }

  @Test
  @DisplayName("이메일 인증 코드 전송 실패 - 유효하지 않은 이메일")
  void requestEmail_fail_validate_email() throws Exception {

    // given
    SendEmailVerificationRequest request = UserRequestStub.toSendEmailVerificationRequestInvalidEmail();
    doNothing().when(userFacade).sendEmailVerification(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/request")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("user-email-verification-request-invalid-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("이메일 인증 코드 전송 실패 - 중복된 이메일")
  void verifyEmail_fail_duplicate_email() throws Exception {

    // given
    SendEmailVerificationRequest request = UserRequestStub.toSendEmailVerificationRequest();
    doThrow(UserException.from(DUPLICATE_EMAIL)).when(userFacade)
        .sendEmailVerification(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/request")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isConflict())
        .andDo(print())
        .andDo(document("user-email-verification-request-duplicate-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("이메일 인증 코드 전송 실패 - 이메일 sender 전송 실패")
  void verifyEmail_fail_email_send_failed() throws Exception {

    // given
    SendEmailVerificationRequest request = UserRequestStub.toSendEmailVerificationRequest();
    doThrow(EmailException.from(EMAIL_SEND_FAILED)).when(userFacade)
        .sendEmailVerification(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/request")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isServiceUnavailable())
        .andDo(print())
        .andDo(document("user-email-verification-request-send-failed",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("이메일 인증 성공")
  void verifyEmail() throws Exception {

    // given
    VerifyEmailRequest request = UserRequestStub.toVerifyEmailRequest();
    doNothing().when(userFacade).verifyEmail(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/verify")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-email-verification-verify",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소"),
                fieldWithPath("code").description("이메일 인증 코드")
            )
        ));
  }

  @Test
  @DisplayName("이메일 인증 실패 - 유효하지 않은 이메일")
  void verifyEmail_fail_validate_email() throws Exception {

    // given
    VerifyEmailRequest request = UserRequestStub.toVerifyEmailRequestInvalidEmail();
    doNothing().when(userFacade).verifyEmail(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/verify")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("user-email-verification-verify-invalid-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소"),
                fieldWithPath("code").description("이메일 인증 코드")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("이메일 인증 실패 - 유효하지 않은 이메일 인증 코드 길이")
  void verifyEmail_fail_invalid_email_code_size() throws Exception {

    // given
    VerifyEmailRequest request = UserRequestStub.toVerifyEmailRequestInvalidCode();
    doNothing().when(userFacade).verifyEmail(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/verify")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("user-email-verification-verify-invalid-email-code-size",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소"),
                fieldWithPath("code").description("이메일 인증 코드")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("이메일 인증 실패 - 유효하지 않은 이메일 인증 코드")
  void verifyEmail_fail_invalid_verification_email_code() throws Exception {

    // given
    VerifyEmailRequest request = UserRequestStub.toVerifyEmailRequest();
    doThrow(UserException.from(INVALID_EMAIL_VERIFICATION)).when(userFacade)
        .verifyEmail(request.toCommand());

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/users/email-verification/verify")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    result.andExpect(status().isUnauthorized())
        .andDo(print())
        .andDo(document("user-email-verification-verify-invalid-verification-email-code",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소"),
                fieldWithPath("code").description("이메일 인증 코드")
            ),
            getErrorResponseFieldSnippet()
        ));
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