package com.rouby.user.infrastructure.security.dto;

import com.rouby.user.domain.entity.UserRole;
import java.util.Collection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Builder
@AllArgsConstructor
public class SecurityUser implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;
    private final UserRole userRole;
    private final Collection<? extends GrantedAuthority> authorities;

    public Long getId() { return id; }

    public UserRole getRole() {
        return userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}