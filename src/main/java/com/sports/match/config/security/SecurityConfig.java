package com.sports.match.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화 (API 개발 시 필요)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/","/login/**", "/common/**", "/login/failure", "/js/**", "/css/**", "/images/**").permitAll() // 로그인, 회원가입은 인증 없이 접근 가능
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/failure?error=true")) // 인증되지 않은 접근 시 /login/failure?error=true로 리다이렉트
                        .accessDeniedHandler(new AccessDeniedHandlerImpl()) // 권한이 없는 접근 시 기본 처리
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/login/signin")
                        .defaultSuccessUrl("/", true)
                        .failureHandler((request, response, exception) -> {
                            response.sendRedirect("/login/failure?error=true");
                        })
                )
                .userDetailsService(customUserDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/login/logout")
                        .logoutSuccessUrl("/") // 로그아웃 후 메인 페이지로 이동
                );

        return http.build();
    }
}