package com.rouby.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.auth.jwt.JwtTokenProvider;
import com.rouby.auth.presentation.dto.request.LoginRequest;
import com.rouby.common.exception.type.ApiErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Component
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider jwtTokenProvider;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
    this.authenticationManager = authenticationManager;
    this.jwtTokenProvider = jwtTokenProvider;
    setFilterProcessesUrl("/api/v1/auth/login");
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request,
      HttpServletResponse response) throws AuthenticationException {
    try {
      LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

      UsernamePasswordAuthenticationToken authToken =
          new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());

      log.info("로그인 시도: {}", loginRequest.email());

      return authenticationManager.authenticate(authToken);

    } catch (IOException e) {
      throw new RuntimeException("로그인 실패", e);
    }
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain, Authentication authResult) throws IOException, ServletException {
    UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();

    String token = jwtTokenProvider.createAccessToken(
        userDetails.getId().toString(),
        userDetails.getRole().toString(),
        userDetails.getUsername() //email
    );

    response.setHeader("Authorization", token);
    log.info("로그인 성공, 토큰 발급: {}", token);
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request,
      HttpServletResponse response, AuthenticationException failed)
      throws IOException, ServletException {
    log.warn("로그인 실패: {}", failed.getMessage());
    response.sendError(
        ApiErrorCode.UNAUTHORIZED.getStatus().value(),
        ApiErrorCode.UNAUTHORIZED.getMessage());
  }
}
