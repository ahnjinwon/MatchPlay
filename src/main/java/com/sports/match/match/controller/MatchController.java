package com.sports.match.match.controller;

import com.sports.match.match.model.dto.AttMemberListDto;
import com.sports.match.match.model.dto.MatchListDto;
import com.sports.match.match.model.dto.QueueDto;
import com.sports.match.match.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/main")
    public String matchMain(Model model){
        List<AttMemberListDto> attMemberList = matchService.getAttMemList();
        model.addAttribute("attMemList", attMemberList);
        for(int courtId=0; courtId<3; courtId++){
            List<MatchListDto> matchMember = matchService.getMatchMember(courtId);
            model.addAttribute("court"+courtId, matchMember);
        }
        return "/match/MatchMain";
    }

    @PostMapping("savequeue")
    @ResponseBody
    public ResponseEntity<?> saveQueue(@RequestBody QueueDto queueDto) {
        return matchService.saveQueue(queueDto);
    }

    @GetMapping("queuelist")
    @ResponseBody
    public ResponseEntity<?> queueList(@RequestParam("courtId") int courtId){
        return matchService.queueList(courtId);
    }

    @GetMapping("attendees")
    @ResponseBody
    public List<AttMemberListDto> getAttendees(){
        return matchService.getAttMemList();
    }
}
