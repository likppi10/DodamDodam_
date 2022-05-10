package com.ssafy.api.config;

import com.ssafy.api.config.jwt.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtProvider jwtProvider;

    protected void configure(HttpSecurity http) throws Exception {

        // basic authentication
        http
                .httpBasic().disable() // BasicAuthenticationFilter 비활성화
                .csrf().disable() // CsrfFilter 비활성화
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); //토큰 기반 인증(세션 X)

        // jwt filter
        http.addFilterBefore(
                new JwtAuthenticationFilter(jwtProvider),
                UsernamePasswordAuthenticationFilter.class
        );

        // authorization
        http.authorizeRequests()
                .antMatchers("/server",
                        "/api/user/{userId}",
                        "/api/user/newpassword",
                        "/api/user/signin",
                        "/api/user/signup",
                        "/actuator/health",
                        "/swagger-ui/index.html",
                        "/api/file/**",
                        "/docs/**"
                ).permitAll()
                .anyRequest().authenticated();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        // static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
        web.ignoring().antMatchers("/api-docs/**", "/swagger-resources/**",
                "/swagger-ui/**", "/webjars/**", "/swagger/**");
    }
}
