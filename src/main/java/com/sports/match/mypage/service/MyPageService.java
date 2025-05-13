package com.sports.match.mypage.service;

import com.sports.match.config.security.CustomUserDetails;
import com.sports.match.mypage.model.dao.MyPageDao;
import com.sports.match.mypage.model.dto.MyInfoDto;
import com.sports.match.util.EmailUtil;
import com.sports.match.util.model.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageDao myPageDao;
    private final RedisTemplate<String, String> redisTemplate;
    private final EmailUtil emailUtil;

    public MyInfoDto getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return myPageDao.getMyInfo(userDetails.getMemNo());
    }

    public int updateProfile(MyInfoDto myInfoDto) {
        int result = myPageDao.updateProfile(myInfoDto);
        return result;
    }

    public boolean sendCode(String email) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        try{
            int code = (int)(Math.random() * 900000) + 100000;
            valueOperations.set(email,String.valueOf(code));
            Email emaildto = new Email();
            emaildto.setMailTitle("MatchPlay 인증번호");
            emaildto.setReceiveAddress(email);
            emaildto.setMailContent("인증번호: "+code);
            emailUtil.sendEmail(emaildto);
            System.out.println("key:"+email+", code:"+code);
            redisTemplate.expire(email,180, TimeUnit.SECONDS);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkKey(String key, String memEmail) {
        try{
            System.out.println(memEmail);
            String realKey = redisTemplate.opsForValue().get(memEmail);
            System.out.println(realKey);
            System.out.println(key);
            return realKey.equals(key);
        } catch (NullPointerException e){
            return false;
        } catch (Exception e){
            e.printStackTrace();
            return  false;
        }
    }

    public int getPw(String memPw) {
        String pw = getMyInfo().getMemPw();

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (encoder.matches(memPw, pw)) {
            return 1;
        } else {
            return 0;
        }
    }
}
