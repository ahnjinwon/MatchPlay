package com.sports.match.auth.service;

import com.sports.match.auth.model.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private LocalDate attDate = LocalDate.now();

    public boolean attend(int memNo) {
        int result = authMapper.attend(memNo, attDate);
        return result>0;
    }
}
