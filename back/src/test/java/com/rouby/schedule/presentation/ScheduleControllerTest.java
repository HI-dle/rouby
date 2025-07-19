package com.rouby.schedule.presentation;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class ScheduleControllerTest extends ControllerTestSupport {
  @WithMockCustomUser
  @DisplayName("스케쥴 생성 API - 성공 201")
  @Test
  void createSchedule() throws Exception {

    var request = CreateScheduleRequestFixture.getSuccessRequest();
    var content = objectMapper.writeValueAsString(request);


    var scheduleId = 1L;
    when(scheduleFacade.createSchedule(any(Long.class), any()))
        .thenReturn(scheduleId);

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/schedules")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
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
                fieldWithPath("alarmOffsetMinutes").description(
                    "일정 전 알림 시간 설정 (5, 10, 15, 30 분 / 1, 2시간 / 1, 2 일 / 1주일 전)"),
                fieldWithPath("startAt").description("시작 일시 (예: 2025-08-30T13:00:00)"),
                fieldWithPath("endAt").description("종료 일시 (예: 2025-08-30T15:00:00)"),
                fieldWithPath("routineActivateDate").description("루틴 활성 시점 (예: 2025-08-10)"),
                fieldWithPath("recurrenceRule").description("반복 규칙"),
                fieldWithPath("recurrenceRule.freq").description("반복 주기 (예: MONTHLY)"),
                fieldWithPath("recurrenceRule.byDay").description("반복 요일 (예: MO)"),
                fieldWithPath("recurrenceRule.interval").description("반복 간격"),
                fieldWithPath("recurrenceRule.until").description("반복 종료일시 (예: 2025-12-30T00:00:00)")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 생성 API - 알람 설정 시간 검증 실패 400")
  @Test
  void createSchedule_failed_alarm_offset_validation() throws Exception {

    // given
    var request = CreateScheduleRequestFixture.getAlarmOffsetFailedRequest();
    var content = objectMapper.writeValueAsString(request);

    doThrow(new IllegalArgumentException("지원되지 않는 알림 설정 시간(분) 정보입니다."))
        .when(scheduleFacade).createSchedule(any(Long.class), any());

    // when
    ResultActions resultActions = mockMvc.perform(post("/api/v1/schedules")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("create-schedule-invalid-alarm-offset-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getErrorResponseFieldSnippet()
        ));
  }
}