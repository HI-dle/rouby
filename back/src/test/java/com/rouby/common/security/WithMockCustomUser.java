package com.rouby.common.security;

import com.rouby.user.domain.entity.UserRole;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1l;
    String email() default "test@example.com";
    String name() default "Rob Winch";
    UserRole role() default UserRole.USER;

}
