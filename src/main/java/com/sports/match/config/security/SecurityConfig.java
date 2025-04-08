package com.sports.match.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                        .requestMatchers("/**", "/login/**", "/login/register").permitAll() // 로그인, 회원가입은 인증 없이 접근 가능
                        .anyRequest().authenticated() // 나머지는 인증 필요
                )
                .formLogin(login -> login
                        .loginProcessingUrl("/login/signin") // ✅ Spring Security가 로그인 처리
                        .defaultSuccessUrl("/login/success", true) // 로그인 성공 후 이동할 페이지
                        .permitAll()
                )
                .userDetailsService(customUserDetailsService)
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/") // 로그아웃 후 메인 페이지로 이동
                );

        return http.build();
    }
}

//이후 추가할거 customdetaildto만들어서 추가, 비밀번호 암호화 추가