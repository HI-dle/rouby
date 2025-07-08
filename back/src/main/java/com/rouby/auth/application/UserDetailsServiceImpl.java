package com.rouby.auth.application;

import com.rouby.auth.dto.UserDetailsImpl;
import com.rouby.common.exception.CustomException;
import com.rouby.user.application.exception.UserErrorCode;
import com.rouby.user.domain.entity.User;
import com.rouby.user.domain.repository.UserRepository;
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

  private final UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username)
        .orElseThrow(() -> CustomException.from(UserErrorCode.INVALID_USER));

    Collection<? extends GrantedAuthority> authorities =
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

    return UserDetailsImpl.builder()
        .id(user.getId())
        .email(user.getEmail())
        .password(user.getPassword())
        .userRole(user.getRole())
        .authorities(authorities)
        .build();
  }
}
