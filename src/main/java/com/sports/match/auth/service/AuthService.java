package com.sports.match.auth.service;

import com.sports.match.auth.model.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.binding.BindingException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private LocalDate attDate = LocalDate.now();

    public boolean attend(int memNo) {
        int result = authMapper.attend(memNo, attDate);
        return result>0;
    }

    public boolean getAttend(int memNo) {
        int result;
        try {
            result = authMapper.getAttend(memNo, attDate);
        } catch (BindingException e) {
            result = 0;
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return result>0;
    }

    public boolean attendCancel(int memNo) {
        int result = authMapper.attendCancel(memNo, attDate);
        return result>0;
    }

    public List<String> getAllAtt() {
        List<String> allAtt = authMapper.getAllAtt(attDate);
        System.out.println(allAtt.toString());
        return allAtt;
    }

    public int getMemSize() {
        return authMapper.getMemSize();
    }
}
