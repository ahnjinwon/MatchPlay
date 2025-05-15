package com.sports.match.login.service;

import com.sports.match.login.model.dao.LoginDao;
import com.sports.match.login.model.dto.RegistDto;
import com.sports.match.util.EmailUtil;
import com.sports.match.util.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginDao loginDao;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailUtil emailUtil;

    public void registMember(RegistDto registDto) {
        loginDao.registMember(registDto);
    }

    public boolean checkById(String memId) {
        return loginDao.checkById(memId) > 0;
    }

    public int getCode(String memEmail) {
        int code = (int)(Math.random() * 900000) + 100000;

        try {
            ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
            String key = memEmail;
            System.out.println("Generated Key: " + memEmail);
            valueOperations.set(key, String.valueOf(code));
            Email email = new Email();
            email.setMailTitle("MatchPlay 인증번호");
            email.setReceiveAddress(memEmail);
            email.setMailContent("인증번호: "+code);
            emailUtil.sendEmail(email);
            redisTemplate.expire(key,180, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;
    }

    public boolean checkKey(String key, String memEmail) {
        try{
            String realKey = redisTemplate.opsForValue().get(memEmail);
            return realKey.equals(key);
        } catch (NullPointerException e){
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public boolean checkEmail(String memEmail, String memName) {
        String memId = loginDao.checkEmail(memEmail, memName);
        if(memId==null){
            return false;
        }
        try {
            Email email = new Email();
            email.setReceiveAddress(memEmail);
            email.setMailTitle("MatchPlay에서 아이디를 조회합니다.");
            email.setMailContent("회원님의 id는 <"+memId+"> 입니다.");
            emailUtil.sendEmail(email);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
