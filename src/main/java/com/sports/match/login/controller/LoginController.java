package com.sports.match.login.controller;

import com.sports.match.login.model.dto.FindPwDto;
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
        return "/login/register";
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

    @GetMapping("/checkEmail")
    @ResponseBody
    public Map<String, Integer> checkEmail(@RequestParam("memEmail") String memEmail){
        int code = loginService.getCode(memEmail);
        return Map.of(memEmail,code);
    }

    @GetMapping("/checkKey")
    @ResponseBody
    public boolean checkKey(@RequestParam("key") String key, @RequestParam("memEmail") String memEmail){
        return loginService.checkKey(key,memEmail);
    }

    @GetMapping("loginpage")
    public String loginpage(){
        return "/login/loginPage";
    }

    @GetMapping("findId")
    public String findId(){
        return "/login/findId";
    }

    @PostMapping("findId")
    @ResponseBody
    public boolean findId(@RequestParam("memEmail") String memEmail, @RequestParam("memName") String memName){
        return loginService.checkEmail(memEmail, memName);
    }

    @GetMapping("findPw")
    public String findPw(){
        return "/login/findPw";
    }

    @PostMapping("findPw")
    @ResponseBody
    public boolean findPw(FindPwDto findPwDto){
        return loginService.checkPw(findPwDto);
    }
}
