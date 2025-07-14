package com.rouby.common.security;


import com.rouby.user.infrastructure.security.dto.SecurityUser;
import java.util.Collections;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUserPrincipal) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        SecurityUser principal = SecurityUser.builder()
                .id(customUserPrincipal.id())
                .email(customUserPrincipal.email())
                .build();

        var authorities = Collections.
                singletonList(new SimpleGrantedAuthority(customUserPrincipal.role().name()));

        Authentication auth =
                UsernamePasswordAuthenticationToken.authenticated(principal, "password", authorities);
        context.setAuthentication(auth);
        return context;
    }
}
