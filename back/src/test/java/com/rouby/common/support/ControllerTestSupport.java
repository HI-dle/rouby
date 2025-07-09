package com.rouby.common.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.auth.application.facade.AuthFacade;
import com.rouby.auth.filter.JwtAuthenticationFilter;
import com.rouby.auth.presentation.AuthController;
import com.rouby.schedule.application.facade.ScheduleFacade;
import com.rouby.schedule.presentation.ScheduleController;
import com.rouby.user.application.UserFacade;
import com.rouby.user.domain.entity.User;
import com.rouby.user.presentation.UserController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@WebMvcTest(
    controllers = {ScheduleController.class, UserController.class, AuthController},
    excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE) //, classes = JwtAuthenticationFilter.class)
    }
)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockitoBean
  protected UserFacade userFacade;

  @MockitoBean
  protected ScheduleFacade scheduleFacade;

  @MockitoBean
  protected AuthFacade authFacade;

  @MockitoBean
  protected AuthenticationManager authenticationManager;

  @MockitoBean
  protected JwtAuthenticationFilter jwtAuthenticationFilter;

}