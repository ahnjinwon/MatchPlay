package com.sports.match.main.controller;

import com.sports.match.auth.service.AuthService;
import com.sports.match.config.security.CustomUserDetails;
import com.sports.match.main.service.testService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MainController {
    private testService testservice;
    private final AuthService authService;
    @RequestMapping(value = {"/", "/home"})
    public String main(Authentication authentication, Model model) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            //authService.mailtest(userDetails.getMemEmail());
            boolean result = authService.getAttend(userDetails.getMemNo());
            model.addAttribute("att",result);
        }
        List<String> allAtt = authService.getAllAtt();
        authService.redistest();
        int memSize = authService.getMemSize();
        model.addAttribute("all_att",allAtt);
        model.addAttribute("att_size",allAtt.size());
        model.addAttribute("mem_size",memSize);
        return "common/main";
    }
}
