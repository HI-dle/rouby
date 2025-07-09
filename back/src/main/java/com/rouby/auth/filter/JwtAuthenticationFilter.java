package com.rouby.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.auth.jwt.JwtTokenProvider;
import com.rouby.auth.presentation.dto.request.LoginRequest;
import com.rouby.common.exception.type.ApiErrorCode;
import com.rouby.user.domain.entity.UserRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = jwtTokenProvider.resolveToken(request);

      if (token != null && jwtTokenProvider.validateToken(token)) {

        Long userId = jwtTokenProvider.getUserId(token);
        String email = jwtTokenProvider.getEmail(token);
        String role = jwtTokenProvider.getRole(token);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
            .id(userId)
            .email(email)
            .password(null)
            .userRole(UserRole.valueOf(role))
            .authorities(List.of(new SimpleGrantedAuthority(role)))
            .build();

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("인증 완료: userId={}, email={}", userId, email);
      }

    } catch (Exception ex) {
      log.warn("JWT 필터 인증 실패: {}", ex.getMessage());
      response.sendError(ApiErrorCode.UNAUTHORIZED.getStatus().value(),
          ApiErrorCode.UNAUTHORIZED.getMessage());
      return;
    }

    filterChain.doFilter(request, response);
  }

}
