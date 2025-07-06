package com.rouby.schedule.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.schedule.application.facade.ScheduleFacade;
import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest;
import com.rouby.schedule.presentation.dto.request.CreateScheduleRequest.RecurrenceRuleRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.ResultActions;

class ScheduleControllerTest extends ControllerTestSupport {

  @MockitoBean
  ScheduleFacade scheduleFacade;

  @BeforeEach
  void setUp() {
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 생성 API")
  @Test
  void createSchedule() throws Exception {

    // given
    var request = CreateScheduleRequest.builder()
        .title("하이들 모임!")
        .memo("뭉티기 먹을 것!")
        .alarmOffsetMinutes(1440)
        .startDate(LocalDate.of(2025, 7, 21))
        .startTime(LocalTime.of(10, 30))
        .endDate(LocalDate.of(2025, 7, 21))
        .endTime(LocalTime.of(22, 30))
        .routineActivateDate(LocalDate.of(2025, 7, 7))
        .recurrenceRule(RecurrenceRuleRequest.builder()
            .freq("MONTHLY")
            .interval(1)
            .byDay("MO")
            .until(LocalDateTime.of(2025,12,30, 0, 0))
            .build())
        .build();
    var content = objectMapper.writeValueAsString(request);

    var scheduleId = 1L;
    when(scheduleFacade.createSchedule(any(Long.class), eq(request.toCommand())))
        .thenReturn(scheduleId);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/schedules")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isCreated())
        .andExpect(header().string(
            "Location", Matchers.endsWith(String.format("/api/v1/schedules/%s", scheduleId))))
        .andDo(print())
        .andDo(document("create-schedule-201",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("title").description("일정 제목"),
                fieldWithPath("memo").description("일정 메모"),
                fieldWithPath("alarmOffsetMinutes").description("일정 전 알림 시간 설정 (5, 10, 15, 30 분 / 1, 2시간 / 1, 2 일 / 1주일 전"),
                fieldWithPath("startDate").description("시작 일자 (예: 2025-08-30)"),
                fieldWithPath("startTime").description("시작 시간 (예: 13:00:00)"),
                fieldWithPath("endDate").description("종료 일자 (예: 2025-08-30)"),
                fieldWithPath("endTime").description("종료 시간 (예: 15:00:00)"),
                fieldWithPath("routineActivateDate").description("루틴 활성 시점 (예: 2025-08-10)"),
                fieldWithPath("recurrenceRule").description("반복 규칙"),
                fieldWithPath("recurrenceRule.freq").description("반복 주기 (예: MONTHLY)"),
                fieldWithPath("recurrenceRule.byDay").description("반복 요일 (예: MO)"),
                fieldWithPath("recurrenceRule.interval").description("반복 간격"),
                fieldWithPath("recurrenceRule.until").description("반복 종료일 (예: 2025-12-30T00:00:00)")
            )
        ));
  }
}