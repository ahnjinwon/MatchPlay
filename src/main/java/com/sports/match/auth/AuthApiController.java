package com.sports.match.auth;

import com.sports.match.auth.service.AuthService;
import com.sports.match.config.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
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
}//출석체크 db 생성, 본인 출석 여부 불러와서 메인에 자동 적용, 출석하기 활성화, 출석 통계 적용, 출석완료하면 출석취소로 변경 및 적용
