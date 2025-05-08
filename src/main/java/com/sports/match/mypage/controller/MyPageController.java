package com.sports.match.mypage.controller;

import com.sports.match.mypage.model.dto.MyInfoDto;
import com.sports.match.mypage.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("updateProfile")
    public String updateProfile(Model model, Authentication authentication){
        MyInfoDto myInfoDto = mypageService.getMyInfo();
        model.addAttribute("showSidebar",true);
        model.addAttribute("myInfo",myInfoDto);
        return "/mypage/updateProfile";
    }

    @PostMapping("updateProfile")
    public String updateProfile(MyInfoDto myInfoDto){
        System.out.println(myInfoDto.toString());
        int result=mypageService.updateProfile(myInfoDto);
        return "redirect:/mypage/mainpage";
    }

    @GetMapping("updateCancel")
    public String updateCancel(){
        return "redirect:/mypage/mainpage";
    }

    @GetMapping("sendCode")
    @ResponseBody
    public boolean sendCode(@RequestParam("email") String email) {
        return mypageService.sendCode(email);
    }

    @GetMapping("checkKey")
    @ResponseBody
    public boolean checkKey(@RequestParam("key") String key, @RequestParam("memEmail") String memEmail){
        return mypageService.checkKey(key, memEmail);
    }
}
