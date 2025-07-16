package com.rouby.routine.routine_task.presentaion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.routine.routine_task.presentaion.dto.request.CreateRoutineTaskRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RoutineTaskControllerTest extends ControllerTestSupport {

  @WithMockCustomUser
  @Test
  @DisplayName("루틴 테스크 생성 API - 성공 201")
  void createRoutineTask() throws Exception {
    // given
    var request = new CreateRoutineTaskRequest(
        "물 마시기",
        CreateRoutineTaskRequest.TaskType.COUNT,
        8,
        15,
        LocalDate.of(2025, 7, 15),
        LocalTime.of(8, 0),
        List.of(CreateRoutineTaskRequest.Weekday.MO, CreateRoutineTaskRequest.Weekday.WE, CreateRoutineTaskRequest.Weekday.FR),
        ZonedDateTime.parse("2025-08-15T22:30:00+09:00[Asia/Seoul]")
    );

    String content = objectMapper.writeValueAsString(request);
    Long routineTaskId = 42L;

    given(routineTaskFacade.createRoutineTask(any())).willReturn(routineTaskId);

    // when
    ResultActions result = mockMvc.perform(post("/api/v1/routine-task")
        .header("Authorization", "Bearer ACCESS_TOKEN")
        .contentType(MediaType.APPLICATION_JSON)
        .content(content)
    );

    // then
    result.andExpect(status().isCreated())
        .andExpect(header().string("Location", Matchers.endsWith("/api/v1/routine-task/" + routineTaskId)))
        .andDo(print())
        .andDo(document("routine-task-create-201",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("title").description("루틴 제목"),
                fieldWithPath("taskType").description("태스크 타입 (COUNT, MINUTES, CHECK)"),
                fieldWithPath("targetValue").description("목표값"),
                fieldWithPath("alarmOffsetMinutes").description("알림 오프셋 시간 (분)"),
                fieldWithPath("startDate").description("루틴 시작일 (예: 2025-07-15)"),
                fieldWithPath("time").description("루틴 실행 시간 (예: 08:00:00)"),
                fieldWithPath("byDays").description("루틴 반복 요일 리스트 (예: [MO, WE, FR])"),
                fieldWithPath("until").description("루틴 종료일 (예: 2025-08-15T22:30:00+09:00[Asia/Seoul])")
            )
        ));
  }
}