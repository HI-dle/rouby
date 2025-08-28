package com.rouby.routine.routine_task.presentaion;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.routine.routine_task.application.dto.info.GetRoutineTaskInfo;
import com.rouby.routine.routine_task.presentaion.dto.request.CreateRoutineTaskRequest;
import com.rouby.routine.routine_task.presentaion.dto.request.GetRoutineTaskRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
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
        Set.of(CreateRoutineTaskRequest.Weekday.MO, CreateRoutineTaskRequest.Weekday.WE, CreateRoutineTaskRequest.Weekday.FR),
        LocalDateTime.parse("2025-08-15T22:30:00")
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
                fieldWithPath("untilDate").description("루틴 종료일 (예: 2025-08-15T22:30:00+09:00[Asia/Seoul])")
            )
        ));
  }
  @WithMockCustomUser
  @Test
  @DisplayName("루틴 태스크 조회 API - 성공 200")
  void getRoutineTask() throws Exception {
    // given
    LocalDate fromDate = LocalDate.of(2025, 8, 1);
    LocalDate toDate = LocalDate.of(2025, 8, 30);

    GetRoutineTaskRequest request = new GetRoutineTaskRequest(fromDate, toDate);

    GetRoutineTaskInfo.RoutineTaskOverrideDto overrideDto = GetRoutineTaskInfo.RoutineTaskOverrideDto.builder()
        .id(100L)
        .title("오버라이드 제목")
        .routineTimeInfo(
            GetRoutineTaskInfo.RoutineTimeInfoDto.builder()
                .startDate(LocalDate.of(2025, 8, 5))
                .untilDate(LocalDate.of(2025, 8, 10))
                .time(LocalTime.of(9, 30))
                .weekdays(Set.of("MO", "WE"))
                .build()
        )
        .overrideType("MODIFIED")
        .overrideTypeDesc("수정됨")
        .overrideDate(LocalDate.of(2025, 8, 6))
        .build();

    GetRoutineTaskInfo.DailyProgressDto dailyProgress1 = GetRoutineTaskInfo.DailyProgressDto.builder()
        .dailyTaskId(1L)
        .taskDate(LocalDate.of(2025, 8, 1))
        .currentValue(80)
        .build();

    GetRoutineTaskInfo.DailyProgressDto dailyProgress2 = GetRoutineTaskInfo.DailyProgressDto.builder()
        .dailyTaskId(2L)
        .taskDate(LocalDate.of(2025, 8, 3))
        .currentValue(100)
        .build();

    GetRoutineTaskInfo.DailyProgressDto dailyProgress3 = GetRoutineTaskInfo.DailyProgressDto.builder()
        .dailyTaskId(3L)
        .taskDate(LocalDate.of(2025, 8, 5))
        .currentValue(50)
        .build();

    GetRoutineTaskInfo.RoutineTask routineTaskDto = GetRoutineTaskInfo.RoutineTask.builder()
        .id(1L)
        .userId(1L)
        .title("물 마시기")
        .taskType("COUNT")
        .alarmOffsetMinutes(5)
        .routineTimeInfo(
            GetRoutineTaskInfo.RoutineTimeInfoDto.builder()
                .startDate(LocalDate.of(2025, 8, 1))
                .untilDate(LocalDate.of(2025, 8, 30))
                .time(LocalTime.of(8, 0))
                .weekdays(Set.of("MO", "WE", "FR"))
                .build()
        )
        .recurrenceRule(
            GetRoutineTaskInfo.RecurrenceRuleDto.builder()
                .freq("WEEKLY")
                .byDay(Set.of("MO", "WE", "FR"))
                .interval(1)
                .until(LocalDate.of(2025, 8, 30))
                .rruleStr("FREQ=WEEKLY;BYDAY=MO,WE,FR;INTERVAL=1")
                .build()
        )
        .routineOverrides(List.of(overrideDto))
        .dailyProgress(List.of(dailyProgress1, dailyProgress2, dailyProgress3))
        .build();

    GetRoutineTaskInfo mockResponse = GetRoutineTaskInfo.builder()
        .routines(List.of(routineTaskDto))
        .build();

    given(routineTaskFacade.getRoutineTaskWithProgress(any())).willReturn(mockResponse);

    // when
    ResultActions result = mockMvc.perform(get("/api/v1/routine-task")
        .param("fromDate", request.fromDate().toString())
        .param("toDate", request.toDate().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer ACCESS_TOKEN")
    );

    // then
    result.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("routine-task-get-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("routineTasks").description("루틴 태스크 목록"),
                fieldWithPath("routineTasks[].id").description("루틴 태스크 ID"),
                fieldWithPath("routineTasks[].userId").description("사용자 ID"),
                fieldWithPath("routineTasks[].title").description("루틴 제목"),
                fieldWithPath("routineTasks[].taskType").description("루틴 타입 (COUNT, CHECK 등)"),
                fieldWithPath("routineTasks[].alarmOffsetMinutes").description("알람 옵셋 분 (예: 5분 전 알림)"),
                fieldWithPath("routineTasks[].routineTimeInfo.startDate").description("루틴 시작일"),
                fieldWithPath("routineTasks[].routineTimeInfo.untilDate").description("루틴 종료일"),
                fieldWithPath("routineTasks[].routineTimeInfo.time").description("루틴 수행 시간"),
                fieldWithPath("routineTasks[].routineTimeInfo.weekdays").description("반복 요일 (예: [MO, TU, WE])"),
                fieldWithPath("routineTasks[].recurrenceRule.freq").description("반복 빈도 (예: DAILY, WEEKLY)"),
                fieldWithPath("routineTasks[].recurrenceRule.byDay").description("반복 요일 (예: [MO, TU])"),
                fieldWithPath("routineTasks[].recurrenceRule.interval").description("반복 간격"),
                fieldWithPath("routineTasks[].recurrenceRule.untilDate").description("반복 종료일"),
                fieldWithPath("routineTasks[].recurrenceRule.rruleStr").description("RRULE 문자열"),
                fieldWithPath("routineTasks[].routineOverrides").description("오버라이드 목록"),
                fieldWithPath("routineTasks[].routineOverrides[].id").description("오버라이드 ID"),
                fieldWithPath("routineTasks[].routineOverrides[].title").description("오버라이드 루틴 제목"),
                fieldWithPath("routineTasks[].routineOverrides[].overrideType").description("오버라이드 타입"),
                fieldWithPath("routineTasks[].routineOverrides[].overrideTypeDesc").description("오버라이드 타입 설명"),
                fieldWithPath("routineTasks[].routineOverrides[].overrideDate").description("오버라이드 날짜"),
                fieldWithPath("routineTasks[].routineOverrides[].routineTimeInfo.startDate").description("오버라이드 시작일"),
                fieldWithPath("routineTasks[].routineOverrides[].routineTimeInfo.untilDate").description("오버라이드 종료일"),
                fieldWithPath("routineTasks[].routineOverrides[].routineTimeInfo.time").description("오버라이드 수행 시간"),
                fieldWithPath("routineTasks[].routineOverrides[].routineTimeInfo.weekdays").description("오버라이드 반복 요일"),
                fieldWithPath("routineTasks[].dailyProgress").description("일일 진행 목록"),
                fieldWithPath("routineTasks[].dailyProgress[].dailyTaskId").description("데일리 태스크 ID"),
                fieldWithPath("routineTasks[].dailyProgress[].taskDate").description("진행 기록 날짜"),
                fieldWithPath("routineTasks[].dailyProgress[].currentValue").description("현재 진행 값")
            )
        ));
  }
}