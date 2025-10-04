package com.rouby.schedule.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.rouby.common.security.WithMockCustomUser;
import com.rouby.common.support.ControllerTestSupport;
import com.rouby.schedule.fixture.CreateScheduleRequestFixture;
import com.rouby.schedule.fixture.DeleteScheduleFromRequestFixture;
import com.rouby.schedule.fixture.DeleteScheduleRequestFixture;
import com.rouby.schedule.fixture.GetScheduleRequestFixture;
import com.rouby.schedule.fixture.SchedulesInfoFixture;
import com.rouby.schedule.fixture.UpdateScheduleRequestFixture;
import java.time.LocalDateTime;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

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
                fieldWithPath("routineOffsetDays").description("루틴 활성 일시 간격 (예: 10)"),
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

  @WithMockCustomUser
  @DisplayName("스케쥴 기간 조회 API - 성공 200")
  @Test
  void getSchedules() throws Exception {

    var request = GetScheduleRequestFixture.getSuccessRequest();
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("fromAt", request.fromAt().toString());
    params.add("toAt", request.toAt().toString());

    var schedulesInfo = SchedulesInfoFixture.createSchedulesInfo();

    when(scheduleFacade.getSchedules(request.toQuery(1L)))
        .thenReturn(schedulesInfo);

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/v1/schedules")
            .params(params)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .characterEncoding("UTF-8")
    );

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("get-schedules-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("fromAt").description("조회 시작 일시"),
                parameterWithName("toAt").description("조회 종료 일시")
            ),
            responseFields(
                fieldWithPath("schedules[].id").description("일정 아이디"),
                fieldWithPath("schedules[].userId").description("유저 아이디"),
                fieldWithPath("schedules[].title").description("일정 제목"),
                fieldWithPath("schedules[].memo").description("일정 메모"),
                fieldWithPath("schedules[].alarmOffsetMinutes").description(
                    "일정 전 알림 시간 설정 (5, 10, 15, 30 분 / 1, 2시간 / 1, 2 일 / 1주일 전)"),
                fieldWithPath("schedules[].startAt").description("시작 일시 (예: 2025-08-30T13:00:00)"),
                fieldWithPath("schedules[].endAt").description("종료 일시 (예: 2025-08-30T15:00:00)"),
                fieldWithPath("schedules[].routineOffsetDays").description("루틴 활성 일시 간격 (예: 10)"),
                fieldWithPath("schedules[].recurrenceRule").description("반복 규칙"),
                fieldWithPath("schedules[].recurrenceRule.freq").description("반복 주기 (예: MONTHLY)"),
                fieldWithPath("schedules[].recurrenceRule.byDay").description("반복 요일 (예: MO)"),
                fieldWithPath("schedules[].recurrenceRule.interval").description("반복 간격"),
                fieldWithPath("schedules[].recurrenceRule.until").description("반복 종료일시 (예: 2025-12-30T00:00:00)"),
                fieldWithPath("schedules[].recurrenceRule.rruleStr").description("rrule 문자열 (예: FREQ=WEEKLY;BYDAY=MO,TU,WE,TH;INTERVAL=1;DTSTART=20250721T030000Z)"),
                fieldWithPath("schedules[].scheduleOverrides[].id").description("예외 일정 아이디"),
                fieldWithPath("schedules[].scheduleOverrides[].userId").description("예외 일정 유저 아이디"),
                fieldWithPath("schedules[].scheduleOverrides[].title").description("예외 일정 제목"),
                fieldWithPath("schedules[].scheduleOverrides[].memo").description("예외 일정 메모"),
                fieldWithPath("schedules[].scheduleOverrides[].routineOffsetDays").description("예외 일정 루틴 활성 일시 간격 (예: 10)"),
                fieldWithPath("schedules[].scheduleOverrides[].alarmOffsetMinutes").description(
                    "예외 일정 전 알림 시간 설정 (5, 10, 15, 30 분 / 1, 2시간 / 1, 2 일 / 1주일 전)"),
                fieldWithPath("schedules[].scheduleOverrides[].startAt").description("예외 변경 일정 시작 일시 (예: 2025-12-30T00:00:00)"),
                fieldWithPath("schedules[].scheduleOverrides[].endAt").description("예외 변경 일정 종료 일시 (예: 2025-12-30T00:00:00)"),
                fieldWithPath("schedules[].scheduleOverrides[].overrideType").description("예외 반복 변경 타입 (MODIFIED, CANCELED)"),
                fieldWithPath("schedules[].scheduleOverrides[].overrideDate").description("예외 반복 변경 일자 (예: 2025-12-30)")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 기간 조회 API - 유효하지 않은 기간 파라미터로 인한 실패 400")
  @Test
  void getSchedulesFailed_invalidRequest() throws Exception {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("fromAt", LocalDateTime.of(2025, 7, 20, 0, 0).toString());
    params.add("toAt", LocalDateTime.of(2025, 7, 15, 0, 0).toString());

    // when
    ResultActions resultActions = mockMvc.perform(get("/api/v1/schedules")
        .params(params)
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .characterEncoding("UTF-8")
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("get-schedules-invalid-request-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            queryParameters(
                parameterWithName("fromAt").description("조회 시작 일시"),
                parameterWithName("toAt").description("조회 종료 일시")
            ),
            getErrorResponseFieldSnippet()
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 수정 API - 성공 200")
  @Test
  void updateSchedule() throws Exception {

    // given
    var request = UpdateScheduleRequestFixture.getSuccessRequest();
    var content = objectMapper.writeValueAsString(request);

    when(scheduleFacade.updateSchedule(any(Long.class), any()))
        .thenReturn(request.targetScheduleId());

    // when
    ResultActions resultActions = mockMvc.perform(put("/api/v1/schedules")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isOk())
        .andDo(print())
        .andDo(document("update-schedule-200",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("parentScheduleId").description("부모 일정 아이디"),
                fieldWithPath("targetScheduleId").description("수정할 타겟 일정 아이디"),
                fieldWithPath("title").description("일정 제목"),
                fieldWithPath("memo").description("일정 메모"),
                fieldWithPath("alarmOffsetMinutes").description(
                    "일정 전 알림 시간 설정 (5, 10, 15, 30 분 / 1, 2시간 / 1, 2 일 / 1주일 전)"),
                fieldWithPath("overrideDate").description("예외 반복 변경 일자 (예: 2025-09-15)"),
                fieldWithPath("startAt").description("수정된 시작 일시 (예: 2025-09-15T10:00:00)"),
                fieldWithPath("endAt").description("수정된 종료 일시 (예: 2025-09-15T12:00:00)")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 수정 API - 알람 설정 시간 검증 실패 400")
  @Test
  void updateSchedule_failed_alarm_offset_validation() throws Exception {

    // given
    var request = UpdateScheduleRequestFixture.getAlarmOffsetFailedRequest();
    var content = objectMapper.writeValueAsString(request);

    doThrow(new IllegalArgumentException("지원되지 않는 알림 설정 시간(분) 정보입니다."))
        .when(scheduleFacade).updateSchedule(any(Long.class), any());

    // when
    ResultActions resultActions = mockMvc.perform(put("/api/v1/schedules")
        .header("Authorization", "Bearer {ACCESS_TOKEN}")
        .content(content)
        .characterEncoding("UTF-8")
        .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("update-schedule-invalid-alarm-offset-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getErrorResponseFieldSnippet()
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 단일 삭제 API - 성공 200")
  @Test
  void deleteSchedule() throws Exception {

    // given
    var request = DeleteScheduleRequestFixture.getSuccessRequest();
    var content = objectMapper.writeValueAsString(request);

    doNothing().when(scheduleFacade).deleteSchedule(any(Long.class), any());

    // when
    ResultActions resultActions = mockMvc.perform(
        delete("/api/v1/schedules") // 실제 컨트롤러 매핑에 맞게 수정
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(content)
            .characterEncoding("UTF-8")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("delete-schedule-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("scheduleId").description("삭제할 일정 아이디"),
                fieldWithPath("instanceDate").description("삭제 기준 instanceDate"),
                fieldWithPath("startAt").description("삭제 대상 시작 일시"),
                fieldWithPath("endAt").description("삭제 대상 종료 일시")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 반복 이후 삭제 API - 성공 200")
  @Test
  void deleteScheduleFrom() throws Exception {

    // given
    var request = DeleteScheduleFromRequestFixture.getSuccessRequest();
    var content = objectMapper.writeValueAsString(request);

    doNothing().when(scheduleFacade).deleteSchedulesStartingFrom(any(Long.class), any());

    // when
    ResultActions resultActions = mockMvc.perform(
        delete("/api/v1/schedules/from")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(content)
            .characterEncoding("UTF-8")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isNoContent())
        .andDo(print())
        .andDo(document("delete-schedule-from-204",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("scheduleId").description("삭제할 일정 아이디"),
                fieldWithPath("fromAt").description("해당 일시 이후 일정부터 삭제")
            )
        ));
  }

  @WithMockCustomUser
  @DisplayName("스케쥴 삭제 API - 유효하지 않은 요청으로 인한 실패 400")
  @Test
  void deleteSchedule_invalidRequest() throws Exception {

    // given
    var request = DeleteScheduleRequestFixture.getInvalidDateRequest();
    var content = objectMapper.writeValueAsString(request);

    doThrow(new IllegalArgumentException("시작 시간이 종료 시간보다 이후일 수 없습니다."))
        .when(scheduleFacade).deleteSchedule(any(Long.class), any());

    // when
    ResultActions resultActions = mockMvc.perform(
        delete("/api/v1/schedules")
            .header("Authorization", "Bearer {ACCESS_TOKEN}")
            .content(content)
            .characterEncoding("UTF-8")
            .contentType(MediaType.APPLICATION_JSON)
    );

    // then
    resultActions.andExpect(status().isBadRequest())
        .andDo(print())
        .andDo(document("delete-schedule-invalid-400",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            getErrorResponseFieldSnippet()
        ));
  }
}

