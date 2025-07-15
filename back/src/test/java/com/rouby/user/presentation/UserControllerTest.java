package com.rouby.user.presentation;

import static com.rouby.notification.email.application.exception.EmailErrorCode.EMAIL_LIMIT_EXCEEDED;
import static com.rouby.notification.email.application.exception.EmailErrorCode.EMAIL_SEND_FAILED;
import static com.rouby.user.application.exception.UserErrorCode.DUPLICATE_EMAIL;
import static com.rouby.user.application.exception.UserErrorCode.EMAIL_NOT_VERIFIED;
import static com.rouby.user.application.exception.UserErrorCode.INVALID_EMAIL_VERIFICATION;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.notification.email.application.exception.EmailErrorCode;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.notification.email.domain.entity.EmailType;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.presentation.dto.request.CreateUserRequest;
import com.rouby.user.presentation.dto.request.FindPasswordRequest;
import com.rouby.user.presentation.dto.request.ResetPasswordByTokenRequest;
import com.rouby.user.presentation.dto.request.ResetPasswordRequest;
import com.rouby.user.presentation.dto.request.SendEmailVerificationRequest;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerTestSupport {

  @Test
  @DisplayName("회원 가입 성공")
  void createUser() throws Exception {

    // given
    CreateUserRequest request = UserRequestFixture.toCreateRequest();
    doNothing().when(userFacade).createUser(request.toCommand());

    // when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
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
  @DisplayName("회원 가입 실패 - 중복된 이메일")
  void createUser_duplicate_email() throws Exception {

    //given
    CreateUserRequest request = UserRequestFixture.toCreateRequest();
    doThrow(UserException.from(DUPLICATE_EMAIL))
        .when(userFacade).createUser(request.toCommand());

    //when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andDo(print())
        .andDo(document("user-create-duplicate-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("중복된 이메일"),
                fieldWithPath("password").description("비밀번호 (8~32자, 2종류 이상 조합)")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("회원 가입 실패 - 인증되지 않은 이메일")
  void createUser_unverified_email() throws Exception {

    //given
    CreateUserRequest request = UserRequestFixture.toCreateRequest();
    doThrow(UserException.from(EMAIL_NOT_VERIFIED))
        .when(userFacade).createUser(request.toCommand());

    //when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized())
        .andDo(document("user-create-unverified-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("이메일 주소 (인증 필요)"),
                fieldWithPath("password").description("비밀번호")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @DisplayName("회원 가입 실패 - 이메일 형식 오류")
  void createUser_invalid_email() throws Exception {

    //given
    CreateUserRequest request = UserRequestFixture.toCreateRequestInvalidEmail();

    //when and then
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
  void createUser_invalid_password() throws Exception {

    //given
    CreateUserRequest request = UserRequestFixture.toCreateRequestInvalidPassword();

    //when and then
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
    SendEmailVerificationRequest request = UserRequestFixture.toSendEmailVerificationRequest();
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
    SendEmailVerificationRequest request =
        UserRequestFixture.toSendEmailVerificationRequestInvalidEmail();
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
    SendEmailVerificationRequest request = UserRequestFixture.toSendEmailVerificationRequest();
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
    SendEmailVerificationRequest request = UserRequestFixture.toSendEmailVerificationRequest();
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
    VerifyEmailRequest request = UserRequestFixture.toVerifyEmailRequest();
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
    VerifyEmailRequest request = UserRequestFixture.toVerifyEmailRequestInvalidEmail();
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
    VerifyEmailRequest request = UserRequestFixture.toVerifyEmailRequestInvalidCode();
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
    VerifyEmailRequest request = UserRequestFixture.toVerifyEmailRequest();
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
  void findPassword_EmailSendLimitExceeded() throws Exception {
    //given
    FindPasswordRequest request = FindPasswordRequest.builder().email("test@email.com").build();

    doNothing().when(userFacade).findPassword(argThat(req ->
        "test@email.com".equals(request.email())));

    //when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/users/password/find")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("find-password-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("email").description("유저 이메일"))
        ));
  }

  @WithMockCustomUser
  @DisplayName("비밀번호 찾기 이메일 전송 제한 실패 API")
  @Test
  void findPassword() throws Exception {
    //given
    FindPasswordRequest request = FindPasswordRequest.builder().email("test@email.com").build();

    doThrow(EmailException.from(EMAIL_LIMIT_EXCEEDED)).when(userFacade).findPassword(request.toCommand());

    //when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/users/password/find")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isTooManyRequests())
        .andDo(print())
        .andDo(document("find-password-email-send-limit-fail",
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
    ResetPasswordByTokenRequest request = ResetPasswordByTokenRequest.builder()
        .newPassword("newPassword")
        .email("test@email.com")
        .token(UUID.randomUUID().toString())
        .build();

    String token = UUID.randomUUID().toString();

    doNothing().when(userFacade).resetPasswordByToken(request.toCommand());

    //when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/password/reset/token")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("reset-password-by-token-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("token").description("비밀번호 변경 토큰"),
                fieldWithPath("email").description("사용자 이메일"),
                fieldWithPath("newPassword").description("새 비밀번호"))
        ));
  }

  @WithMockCustomUser
  @DisplayName("비밀번호 토큰 검증 API")
  @Test
  void validateResetToken() throws Exception {
    String email = "test@email.com";
    String token = UUID.randomUUID().toString();

    doNothing().when(userFacade).validatePasswordToken(email, token);

    //when
    ResultActions resultActions = mockMvc.perform(
        get("/api/v1/users/password/reset/validate")
            .param("email", email)
            .param("token", token)
    );

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("validate-reset-token-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("email").description("사용자 이메일"),
                parameterWithName("token").description("비밀번호 검증 토큰"))
        ));
  }

  @WithMockCustomUser
  @DisplayName("비밀번호 변경 API")
  @Test
  void resetPassword() throws Exception {

    //given
    ResetPasswordRequest request = ResetPasswordRequest.builder()
        .currentPassword("currentPassword!")
        .newPassword("newPassword!")
        .build();

    Long userId = 1L;

    doNothing().when(userFacade).resetPassword(eq(userId), any());

    //when
    ResultActions resultActions = mockMvc.perform(patch("/api/v1/users/password/reset")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("reset-password-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("currentPassword").description("현재 비밀번호"),
                fieldWithPath("newPassword").description("새 비밀번호"))
        ));
  }
}