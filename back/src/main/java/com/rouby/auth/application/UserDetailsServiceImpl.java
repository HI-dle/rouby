package com.rouby.auth.application;

import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.user.application.service.UserReadService;
import com.rouby.user.domain.entity.User;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Date : 2025. 07. 08.
 *
 * @author : hanjihoon
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserReadService userReadService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userReadService.findByEmail(username);

    Collection<? extends GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority(user.getRole().getRoleName()));

    return UserDetailsImpl.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .userRole(user.getRole())
        .authorities(authorities)
        .build();
  }
}
