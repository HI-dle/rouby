package com.rouby.user.presentation;

import static com.rouby.notification.email.application.exception.EmailErrorCode.EMAIL_LIMIT_EXCEEDED;
import static com.rouby.notification.email.application.exception.EmailErrorCode.EMAIL_SEND_FAILED;
import static com.rouby.user.application.exception.UserErrorCode.DUPLICATE_EMAIL;
import static com.rouby.user.application.exception.UserErrorCode.EMAIL_NOT_VERIFIED;
import static com.rouby.user.application.exception.UserErrorCode.INVALID_EMAIL_VERIFICATION;
import static com.rouby.user.application.exception.UserErrorCode.ONBOARDING_STATE_CHANGE_NOT_ALLOWED;
import static com.rouby.user.domain.entity.NotificationType.BRIEFING;
import static com.rouby.user.domain.entity.NotificationType.ROUTINE;
import static com.rouby.user.domain.entity.NotificationType.SCHEDULE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.notification.email.application.exception.EmailException;
import com.rouby.user.application.dto.info.RoubySettingInfo;
import com.rouby.user.application.dto.info.RoubySettingInfo.NotificationSettingInfo;
import com.rouby.user.application.dto.info.UserCheckInfo;
import com.rouby.user.application.exception.UserException;
import com.rouby.user.domain.entity.OnboardingState;
import com.rouby.user.presentation.dto.request.CreateUserRequest;
import com.rouby.user.presentation.dto.request.FindPasswordRequest;
import com.rouby.user.presentation.dto.request.ResetPasswordByTokenRequest;
import com.rouby.user.presentation.dto.request.ResetPasswordRequest;
import com.rouby.user.presentation.dto.request.SendEmailVerificationRequest;
import com.rouby.user.presentation.dto.request.UpdateRoubySettingRequest;
import com.rouby.user.presentation.dto.request.UpdateRoubySettingRequest.NotificationSettingRequest;
import com.rouby.user.presentation.dto.request.UpdateMyUserInfoRequest;
import com.rouby.user.presentation.dto.request.VerifyEmailRequest;
import java.util.Set;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UserControllerTest extends ControllerTestSupport {

  @Test
  @DisplayName("회원 가입 성공")
  void createUser() throws Exception {

    // given
    CreateUserRequest request = UserRequestFixture.toCreateRequest();
    String token = UserRequestFixture.VALID_EMAIL_TOKEN;
    doNothing().when(userFacade).createUser(request.toCommand(token));

    // when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
        )
        .andExpect(status().isCreated())
        .andDo(print())
        .andDo(document("user-create",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("이메일 인증 토큰")
            ),
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
    String token = UserRequestFixture.VALID_EMAIL_TOKEN;
    doThrow(UserException.from(DUPLICATE_EMAIL))
        .when(userFacade).createUser(request.toCommand(token));

    //when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
        )
        .andExpect(status().isConflict())
        .andDo(print())
        .andDo(document("user-create-duplicate-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("이메일 인증 토큰")
            ),
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
    String token = UserRequestFixture.VALID_EMAIL_TOKEN;
    doThrow(UserException.from(EMAIL_NOT_VERIFIED))
        .when(userFacade).createUser(request.toCommand(token));

    //when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
        )
        .andExpect(status().isUnauthorized())
        .andDo(document("user-create-unverified-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("이메일 인증 토큰")
            ),
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
    String token = UserRequestFixture.VALID_EMAIL_TOKEN;

    //when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
        )
        .andExpect(status().isBadRequest())
        .andDo(document("user-create-invalid-email",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("이메일 인증 토큰")
            ),
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
    String token = UserRequestFixture.VALID_EMAIL_TOKEN;

    //when and then
    mockMvc.perform(post("/api/v1/users")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, token)
        )
        .andExpect(status().isBadRequest())
        .andDo(document("user-create-invalid-password",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName(HttpHeaders.AUTHORIZATION).description("이메일 인증 토큰")
            ),
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
    String token = UserRequestFixture.VALID_EMAIL_TOKEN;
    when(userFacade.verifyEmail(request.toCommand())).thenReturn(token);

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
            ),
            responseFields(
                fieldWithPath("token").description("이메일 인증토큰")
            )
        ));
  }

  @Test
  @DisplayName("이메일 인증 실패 - 유효하지 않은 이메일")
  void verifyEmail_fail_validate_email() throws Exception {

    // given
    VerifyEmailRequest request = UserRequestFixture.toVerifyEmailRequestInvalidEmail();

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
  void findPassword_EmailSendLimitExceeded() throws Exception {
    //given
    FindPasswordRequest request = FindPasswordRequest.builder().email("test@email.com").build();

    doThrow(EmailException.from(EMAIL_LIMIT_EXCEEDED)).when(userFacade)
        .findPassword(request.toCommand());

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

  @WithMockCustomUser
  @DisplayName("마이페이지 루비세팅 조회 API")
  @Test
  void getRoubySetting() throws Exception {
    // given
    Long userId = 1L;
    RoubySettingInfo roubySettingInfo = new RoubySettingInfo(
        Set.of("친절함", "직설적임"),
        Set.of(
            new NotificationSettingInfo(SCHEDULE, true),
            new NotificationSettingInfo(ROUTINE, false),
            new NotificationSettingInfo(BRIEFING, true)
        )
    );

    given(userFacade.getRoubySettingInfo(userId)).willReturn(roubySettingInfo);

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/v1/users/rouby-setting")
        .header("Authorization", "Bearer {ACCESS_TOKEN}"));

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-rouby-setting-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("communicationTone").description("루비 커뮤니케이션 톤 목록 "),
                fieldWithPath("notificationSettings").description("알림 설정 목록"),
                fieldWithPath("notificationSettings[].notificationType").description("알림 유형"),
                fieldWithPath("notificationSettings[].enabled").description(
                    "알림 사용 여부 (true: 사용, false: 사용 안 함)")
            )

        ));
  }

  @WithMockCustomUser
  @DisplayName("마이페이지 루비세팅 수정 API")
  @Test
  void updateRoubySetting() throws Exception {
    // given
    Long userId = 1L;

    UpdateRoubySettingRequest request = new UpdateRoubySettingRequest(Set.of("친절함", "직설적임"),
        Set.of(
            new NotificationSettingRequest(SCHEDULE, true),
            new NotificationSettingRequest(ROUTINE, false),
            new NotificationSettingRequest(BRIEFING, true)
        ));

    doNothing().when(userFacade).updateRoubySettings(eq(userId), any());

    // when
    ResultActions resultActions = mockMvc.perform(put("/api/v1/users/rouby-setting")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(objectMapper.writeValueAsString(request))
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("update-rouby-setting-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("communicationTone").description("변경할 루비 커뮤니케이션 톤 목록"),
                fieldWithPath("notificationSettings").description("변경할 알림 설정 목록"),
                fieldWithPath("notificationSettings[].notificationType").description("알림 유형"),
                fieldWithPath("notificationSettings[].enabled").description(
                    "알림 사용 여부 (true: 사용, false: 사용 안 함)")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("회원탈퇴 API")
  @Test
  void deleteUser() throws Exception {
    Long userId = 1L;

    doNothing().when(userFacade).delete(eq(userId));

    // when
    ResultActions resultActions = mockMvc.perform(patch("/api/v1/users/delete")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("delete-user-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint())
        ));
  }

  @WithMockCustomUser
  @DisplayName("로그인 후 정보 조회 API")
  @Test
  void userInfoCheck() throws Exception {
    // given
    Long userId = 1L;

    UserCheckInfo info = UserCheckInfo.builder()
        .id(userId)
        .email("test@example.com")
        .nickname("루비짱")
        .healthStatusKeywords(Set.of("불면증", "두통"))
        .profileKeywords(Set.of("취준", "운동"))
        .communicationTone(Set.of("따뜻하게", "존대"))
        .onboardingState(OnboardingState.USER_INFO_SETTING_BEFORE)
        .build();

    given(userFacade.userInfoCheck(eq(userId)))
        .willReturn(info);

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/v1/users/basic-info")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .contentType(MediaType.APPLICATION_JSON));

    // then
    resultActions
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("user-info-check-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("id").description("유저 ID"),
                fieldWithPath("email").description("이메일"),
                fieldWithPath("nickname").optional().description("닉네임"),
                fieldWithPath("healthStatusKeywords[]").optional().description("건강 상태 키워드 목록 예: [\"아토피\", \"ADHD\"]"),
                fieldWithPath("profileKeywords[]").optional().description("프로필 키워드 목록 예: [\"취준\", \"이직\"]"),
                fieldWithPath("communicationTone[]").optional().description("말투 키워드 목록 예: [\"따듯하게\", \"존대\"]"),
                fieldWithPath("onboardingStatePath").optional().description("온보딩 상태에 따라 이동할 프론트 라우팅 경로")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("유저 정보 설정 완료 API")
  @Test
  void completeUserInfoSetting() throws Exception {
    // given
    Long userId = 1L;
    doNothing().when(userFacade).completeInitialUserInfoSetting(eq(userId));

    // when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/onboarding/user-info/complete")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("complete-user-info-setting-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("유저 정보 설정 완료 실패 - 잘못된 상태 전이")
  @Test
  void completeUserInfoSetting_fail_invalid_state_transition() throws Exception {
    // given
    Long userId = 1L;
    doThrow(UserException.from(ONBOARDING_STATE_CHANGE_NOT_ALLOWED))
        .when(userFacade).completeInitialUserInfoSetting(userId);

    // when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/onboarding/user-info/complete")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("complete-user-info-setting-invalid-state",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @WithMockCustomUser
  @DisplayName("루비 설정 완료 API")
  @Test
  void completeRoubySetting() throws Exception {
    // given
    Long userId = 1L;
    doNothing().when(userFacade).completeInitialRoubySetting(userId);

    // when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/onboarding/rouby/complete")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("complete-rouby-setting-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("루비 설정 완료 실패 - 잘못된 상태 전이")
  @Test
  void completeRoubySetting_fail_invalid_state_transition() throws Exception {
    // given
    Long userId = 1L;
    doThrow(UserException.from(ONBOARDING_STATE_CHANGE_NOT_ALLOWED))
        .when(userFacade).completeInitialRoubySetting(userId);

    // when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/onboarding/rouby/complete")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("complete-rouby-setting-invalid-state",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @WithMockCustomUser
  @DisplayName("회원 정보 수정 API - 204 No Content")
  @Test
  void updateUserInfo() throws Exception {
    // given
    Long userId = 1L;

    UpdateMyUserInfoRequest request = new UpdateMyUserInfoRequest(
        "루비짱",
        Set.of("스트레스", "우울감"),
        Set.of("자기개발", "명상"),
        LocalTime.of(7, 0),
        LocalTime.of(23, 0)
    );

    willDoNothing()
        .given(userFacade)
        .updateMyUserInfo(argThat(command ->
            command.updaterId().equals(userId) &&
                command.nickname().equals("루비짱")
        ));

    // when
    ResultActions resultActions = mockMvc.perform(
        patch("/api/v1/users/user-info")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    );

    // then
    resultActions
        .andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("user-info-update-204",
            preprocessRequest(prettyPrint()),
            requestFields(
                fieldWithPath("nickname").description("닉네임 (최대 20자)"),
                fieldWithPath("healthStatusKeywords[]").description("건강 상태 키워드 목록 예: [\"스트레스\", \"우울감\"]"),
                fieldWithPath("profileKeywords[]").description("프로필 키워드 목록 예: [\"자기개발\", \"명상\"]"),
                fieldWithPath("dailyStartTime").description("하루 시작 시간 (예: 07:00)"),
                fieldWithPath("dailyEndTime").description("하루 종료 시간 (예: 23:00)")
            )
        ));
  }


}