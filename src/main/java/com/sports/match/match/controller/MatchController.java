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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/main")
    public String matchMain(Model model){
        List<AttMemberListDto> attMemberList = matchService.getAttMemList();
        model.addAttribute("attMemList", attMemberList);
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

    @GetMapping("court")
    @ResponseBody
    public ResponseEntity<?> court() {
        Map<Integer, List<MatchListDto>> courtMap = new HashMap<>();

        for (int courtId = 1; courtId <= 3; courtId++) {
            List<MatchListDto> matchMember = matchService.getMatchMember(courtId);
            courtMap.put(courtId, matchMember);
        }

        return ResponseEntity.ok(courtMap);
    }
}
