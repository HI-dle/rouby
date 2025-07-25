package com.rouby.user.infrastructure.security.filter;

import com.rouby.common.exception.type.ApiErrorCode;
import com.rouby.user.application.service.token.TokenProvider;
import com.rouby.user.domain.entity.UserRole;
import com.rouby.user.infrastructure.security.dto.SecurityUser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    try {
      String token = tokenProvider.resolveAccessToken(request);

      if (token != null && tokenProvider.validateAccessToken(token)) {

        Long userId = tokenProvider.getUserId(token);
        String email = tokenProvider.getEmail(token);
        UserRole role = UserRole.valueOf(tokenProvider.getRole(token));

        SecurityUser userDetails = SecurityUser.builder()
            .id(userId)
            .email(email)
            .password(null)
            .userRole(role)
            .authorities(List.of(new SimpleGrantedAuthority(role.getRoleName())))
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
