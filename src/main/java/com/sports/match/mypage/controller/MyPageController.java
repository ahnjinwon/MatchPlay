package com.sports.match.mypage.controller;

import com.sports.match.mypage.model.dto.MyInfoDto;
import com.sports.match.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/mypage")
public class MyPageController {
    private final MyPageService mypageService;
    @GetMapping("mainpage")
    public String myPageMain(Model model){
        MyInfoDto myInfoDto = mypageService.getMyInfo();
        model.addAttribute("showSidebar",true);
        model.addAttribute("myInfo",myInfoDto);
        return "/mypage/mainPage";
    }
}
