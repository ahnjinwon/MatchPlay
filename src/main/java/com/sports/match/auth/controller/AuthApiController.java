package com.sports.match.auth.controller;

import com.sports.match.auth.service.AuthService;
import com.sports.match.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/api")
@RequiredArgsConstructor
public class AuthApiController  {
    private final AuthService authService;

    @GetMapping("/attend")
    public int attend(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean result = authService.attend(userDetails.getMemNo());
        return  result ? 1 : 0;
    }

    @GetMapping("/attendCancel")
    public int attendCancel(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean result = authService.attendCancel(userDetails.getMemNo());
        return  result ? 1 : 0;
    }
}
