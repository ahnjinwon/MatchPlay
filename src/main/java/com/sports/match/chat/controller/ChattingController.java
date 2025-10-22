package com.sports.match.chat.controller;

import com.sports.match.chat.model.dto.userInfoDto;
import com.sports.match.chat.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

@RequiredArgsConstructor
@Controller
@RequestMapping("/chat")
public class ChattingController {
    private final ChattingService chattingService;

    @GetMapping("/main")
    public String chatMain(Model model){
        ArrayList<userInfoDto> info_list = chattingService.getuserInfo();
        model.addAttribute("infoList", info_list);
        return "/chat/main";
    }
}
