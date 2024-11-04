package com.sports.match.main.controller;

import com.sports.match.main.service.testService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
public class MainController {
    @Autowired
    private testService testservice;
    @RequestMapping(value = {"/", "/home"})
    public String main(Model model) {
        int num = testservice.test();
        System.out.println(num);
        return "common/main";
    }
}
