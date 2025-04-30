package com.sports.match.login.service;

import com.sports.match.login.model.dao.LoginDao;
import com.sports.match.login.model.dto.RegistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginDao loginDao;
    private final RedisTemplate<String, Integer> redisTemplate;

    public void registMember(RegistDto registDto) {
        loginDao.registMember(registDto);
    }

    public boolean checkById(String memId) {
        return loginDao.checkById(memId) > 0;
    }

    public int getCode(String memEmail) {
        int code = (int)(Math.random() * 900000) + 100000;
        ValueOperations<String, Integer> valueOperations = redisTemplate.opsForValue();
        String key = "key"+code;
        valueOperations.set(key,code);
        redisTemplate.expire(key,180, TimeUnit.SECONDS);
        return code;
    }
}
