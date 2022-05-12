package com.ssafy.api;

import com.ssafy.core.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;


public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User principal = User.builder()
                .userPk(Long.parseLong(customUser.userPk()))
                .userId(customUser.userId())
                .password(customUser.password())
                .name(customUser.name())
                .build();

        principal.setAuthority();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, customUser.userPk(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}