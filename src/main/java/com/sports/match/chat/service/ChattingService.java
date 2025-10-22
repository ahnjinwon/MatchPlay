package com.sports.match.chat.service;

import com.sports.match.chat.model.dao.ChattingDao;
import com.sports.match.chat.model.dto.userInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChattingService {
    private final ChattingDao chattingDao;

    public ArrayList<userInfoDto> getuserInfo() {
        return chattingDao.getuserInfo();
    }
}
