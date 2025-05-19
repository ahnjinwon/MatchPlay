package com.sports.match.match.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    @GetMapping("/main")
    public String matchMain(Model model){
        // 예시 대기열 데이터 생성 (팀원 2명씩 2팀으로 한 매치)
        List<List<List<String>>> queue = Arrays.asList(
                Arrays.asList(
                        Arrays.asList("짱구", "맹자"),
                        Arrays.asList("철수", "훈이")
                ),
                Arrays.asList(
                        Arrays.asList("유리", "수지"),
                        Arrays.asList("마이콜", "도우너")
                )
        );

        model.addAttribute("queue", queue);
        return "/match/MatchMain";
    }
}
