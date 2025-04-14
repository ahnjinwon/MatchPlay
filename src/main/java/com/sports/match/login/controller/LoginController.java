package com.sports.match.login.controller;

import com.sports.match.login.model.dto.RegistDto;
import com.sports.match.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    @GetMapping("/register")
    public String register(){
        return "/common/register";
    }
    @PostMapping("/register")
    public String regist(RegistDto registDto){
        System.out.println(registDto.toString());
        String encodedPw = passwordEncoder.encode(registDto.getMemPw());
        registDto.setMemPw(encodedPw);
        loginService.registMember(registDto);
        return "redirect:/";
    }
    @GetMapping("/check-id")
    @ResponseBody
    public Map<String, Boolean> checkId(@RequestParam("memId") String memId) {
        boolean exists = loginService.checkById(memId);
        return Map.of("exists", exists);
    }
    @GetMapping("/failure")
    public String loginFail(){
        System.out.println("실패!!");
        return "/common/failure";
    }
}
