package com.rouby.routine.daily_task.presentation;

import static com.rouby.routine.daily_task.application.exception.DailyTaskErrorCode.DAILY_TASK_NOT_FOUND;
import static com.rouby.routine.daily_task.application.exception.DailyTaskErrorCode.DUPLICATE_DAILY_TASK;
import static com.rouby.routine.routine_task.application.exception.RoutineTaskErrorCode.ROUTINE_TASK_ACCESS_DENIED;
import static com.rouby.routine.routine_task.application.exception.RoutineTaskErrorCode.ROUTINE_TASK_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
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
import com.rouby.routine.daily_task.application.exception.DailyTaskException;
import com.rouby.routine.daily_task.presentation.dto.ProgressDailyTaskRequest;
import com.rouby.routine.routine_task.application.exception.RoutineTaskException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class DailyTaskControllerTest extends ControllerTestSupport {

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 성공 - 생성")
  void progressDailyTask_create_success() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.createRequest();
    when(dailyTaskFacade.progressDailyTask(any()))
        .thenReturn(DailyTaskRequestFixture.VALID_ROUTINE_TASK_ID);
    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("daily-task-progress-create-success",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").optional().description("데일리 태스크 ID (생성 시 null)"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID"),
                fieldWithPath("currentValue").description("현재 진행값 (0 이상)"),
                fieldWithPath("taskDate").description("태스크 날짜 (YYYY-MM-DD)")
            )
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 성공 - 수정")
  void progressDailyTask_update_success() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.updateRequest();
    when(dailyTaskFacade.progressDailyTask(any()))
        .thenReturn(DailyTaskRequestFixture.VALID_ROUTINE_TASK_ID);

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print())
        .andDo(document("daily-task-progress-update-success",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").description("기존 데일리 태스크 ID"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID"),
                fieldWithPath("currentValue").description("수정할 진행값"),
                fieldWithPath("taskDate").description("태스크 날짜")
            )
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 존재하지 않는 루틴 태스크")
  void progressDailyTask_fail_routine_task_not_found() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.routineTaskNotExistsRequest();
    doThrow(RoutineTaskException.from(ROUTINE_TASK_NOT_FOUND))
        .when(dailyTaskFacade).progressDailyTask(any());

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andDo(document("daily-task-progress-routine-task-not-found",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").optional().description("데일리 태스크 ID"),
                fieldWithPath("routineTaskId").description("존재하지 않는 루틴 태스크 ID"),
                fieldWithPath("currentValue").description("진행값"),
                fieldWithPath("taskDate").description("태스크 날짜")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 루틴 태스크 접근 권한 없음")
  void progressDailyTask_fail_routine_task_access_denied() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.createRequest();
    doThrow(RoutineTaskException.from(ROUTINE_TASK_ACCESS_DENIED))
        .when(dailyTaskFacade).progressDailyTask(any());

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden())
        .andDo(print())
        .andDo(document("daily-task-progress-routine-task-access-denied",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").optional().description("데일리 태스크 ID"),
                fieldWithPath("routineTaskId").description("접근 권한이 없는 루틴 태스크 ID"),
                fieldWithPath("currentValue").description("진행값"),
                fieldWithPath("taskDate").description("태스크 날짜")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 존재하지 않는 데일리 태스크")
  void progressDailyTask_fail_daily_task_not_found() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.dailyTaskNotExistsRequest();
    doThrow(DailyTaskException.from(DAILY_TASK_NOT_FOUND))
        .when(dailyTaskFacade).progressDailyTask(any());

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andDo(print())
        .andDo(document("daily-task-progress-daily-task-not-found",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").description("존재하지 않는 데일리 태스크 ID"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID"),
                fieldWithPath("currentValue").description("진행값"),
                fieldWithPath("taskDate").description("태스크 날짜")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 중복 생성 시도")
  void progressDailyTask_fail_duplicate_daily_task() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.createRequest();
    doThrow(DailyTaskException.from(DUPLICATE_DAILY_TASK))
        .when(dailyTaskFacade).progressDailyTask(any());

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict())
        .andDo(print())
        .andDo(document("daily-task-progress-duplicate-task",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").optional().description("데일리 태스크 ID (null - 새로 생성 시도)"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID"),
                fieldWithPath("currentValue").description("진행값"),
                fieldWithPath("taskDate").description("이미 존재하는 태스크 날짜")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 유효하지 않은 currentValue (0 이하)")
  void progressDailyTask_fail_negative_value() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.negativeValueRequest();

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("daily-task-progress-negative-value",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").optional().description("데일리 태스크 ID"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID"),
                fieldWithPath("currentValue").description("진행값 (음수 값으로 유효성 검증 실패)"),
                fieldWithPath("taskDate").description("태스크 날짜")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 유효하지 않은 ID (0 이하)")
  void progressDailyTask_fail_invalid_id() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.invalidIdRequest();

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("daily-task-progress-invalid-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").description("데일리 태스크 ID (0 이하 값으로 유효성 검증 실패)"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID"),
                fieldWithPath("currentValue").description("진행값"),
                fieldWithPath("taskDate").description("태스크 날짜")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }

  @Test
  @WithMockCustomUser
  @DisplayName("데일리 태스크 진행 실패 - 유효하지 않은 routineTaskId (0 이하)")
  void progressDailyTask_fail_invalid_routine_task_id() throws Exception {

    // given
    ProgressDailyTaskRequest request = DailyTaskRequestFixture.invalidRoutineTaskIdRequest();

    // when and then
    mockMvc.perform(post("/api/v1/daily-task/progress")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("daily-task-progress-invalid-routine-task-id",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestHeaders(
                headerWithName("Authorization").description("사용자 인증 토큰")
            ),
            requestFields(
                fieldWithPath("id").optional().description("데일리 태스크 ID"),
                fieldWithPath("routineTaskId").description("루틴 태스크 ID (0 이하 값으로 유효성 검증 실패)"),
                fieldWithPath("currentValue").description("진행값"),
                fieldWithPath("taskDate").description("태스크 날짜")
            ),
            getValidationErrorResponseFieldSnippet()
        ));
  }
}