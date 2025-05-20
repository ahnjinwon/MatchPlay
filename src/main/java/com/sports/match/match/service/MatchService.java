package com.sports.match.match.service;

import com.sports.match.match.model.dao.MatchDao;
import com.sports.match.match.model.dto.AttMemberListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchDao matchDao;

    public List<AttMemberListDto> getAttMemList() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return matchDao.getAttMemList(today);
    }
}
