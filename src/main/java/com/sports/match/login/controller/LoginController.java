package com.sports.match.login.controller;

import com.sports.match.login.model.dto.RegistDto;
import com.sports.match.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @GetMapping("/success")
    public String loginSuccess(){
        return "/common/success";
    }
    @GetMapping("/failure")
    public String loginFail(){
        System.out.println("실패!!");
        return "/common/failure";
    }
}
