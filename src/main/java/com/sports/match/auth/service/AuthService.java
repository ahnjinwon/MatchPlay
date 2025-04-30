package com.sports.match.auth.service;

import com.sports.match.auth.model.mapper.AuthMapper;
import com.sports.match.util.EmailUtil;
import com.sports.match.util.model.Email;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.binding.BindingException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthMapper authMapper;
    private LocalDate attDate = LocalDate.now();
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailUtil emailUtil;

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
    //redis 테스트
    public void redistest() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String key = "stringKey";
        valueOperations.set(key,"hello");
        redisTemplate.expire(key,10, TimeUnit.SECONDS);
    }
    //메일 테스트
    public void mailtest(String memEmail){
        Email email = new Email();
        email.setReceiveAddress(memEmail);
        email.setMailTitle("test");
        email.setMailContent("인증번호:");
        try{
            emailUtil.sendEmail(email);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
