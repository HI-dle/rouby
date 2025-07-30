package com.rouby.common.support;

import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.common.config.WebConfig;
import com.rouby.common.config.WebMvcConfig;
import com.rouby.common.exception.GlobalExceptionHandler;
import com.rouby.common.resolver.CustomPageableArgumentResolver;
import com.rouby.routine.daily_task.application.facade.DailyTaskFacade;
import com.rouby.routine.daily_task.presentation.DailyTaskController;
import com.rouby.routine.routine_task.application.facade.RoutineTaskFacade;
import com.rouby.routine.routine_task.presentaion.RoutineTaskController;
import com.rouby.user.application.UserFacade;
import com.rouby.user.infrastructure.security.filter.JwtAuthenticationFilter;
import com.rouby.user.presentation.AuthController;
import com.rouby.schedule.application.facade.ScheduleFacade;
import com.rouby.schedule.presentation.ScheduleController;
import com.rouby.user.presentation.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.payload.ResponseFieldsSnippet;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
    controllers = {
        ScheduleController.class,
        AuthController.class,
        UserController.class,
        RoutineTaskController.class,
        DailyTaskController.class
    },
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebMvcConfig.class
        )
    }
)
@Import({
    WebConfig.class,
    CustomPageableArgumentResolver.class,
    GlobalExceptionHandler.class,
})
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockitoBean
  protected ScheduleFacade scheduleFacade;

  @MockitoBean
  protected UserFacade userFacade;

  @MockitoBean
  protected RoutineTaskFacade routineTaskFacade;

  @MockitoBean
  protected DailyTaskFacade dailyTaskFacade;

  @MockitoBean
  protected AuthenticationManager authenticationManager;

  @MockitoBean
  protected JwtAuthenticationFilter jwtAuthenticationFilter;

  protected static ResponseFieldsSnippet getValidationErrorResponseFieldSnippet() {
    return responseFields(
        fieldWithPath("message").description("에러 메시지"),
        fieldWithPath("code").description("에러 코드"),
        fieldWithPath("errors[].value").description("유효하지 않은 필드명"),
        fieldWithPath("errors[].message").description("유효성 검증 실패 메시지")
    );
  }

  protected static ResponseFieldsSnippet getErrorResponseFieldSnippet() {
    return responseFields(
        fieldWithPath("message").description("에러 메시지"),
        fieldWithPath("code").description("에러 코드")
    );
  }

}