package com.sports.match.auth.service;

import com.sports.match.auth.model.mapper.AuthMapper;
import com.sports.match.config.security.CustomUserDetails;
import com.sports.match.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.binding.BindingException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void registQueue(CustomUserDetails userDetails) {
        String memId = userDetails.getUsername();
        String memName = userDetails.getMemName();
        String grade = userDetails.getGrade();

        //출석 할때 redis에 저장
        String setKey = "attendees";
        String hashKey = "attendee:info:" + memId;

        // 참가자 ID Set에 추가
        redisTemplate.opsForSet().add(setKey, memId);

        // 참가자 정보 Hash에 저장
        Map<String, String> infoMap = new HashMap<>();
        infoMap.put("memName", memName);
        infoMap.put("grade", grade);
        infoMap.put("status", "0");
        redisTemplate.opsForHash().putAll(hashKey, infoMap);

        // 오늘 밤 12시 기준 만료 시간 설정
        LocalDateTime midnight = LocalDate.now().plusDays(1).atStartOfDay();
        Instant expireAt = midnight.atZone(ZoneId.systemDefault()).toInstant();

        // Set과 Hash 모두 자정에 만료되도록 설정
        redisTemplate.expireAt(setKey, expireAt);
        redisTemplate.expireAt(hashKey, expireAt);
    }

    public void deleteQueue(CustomUserDetails userDetails) {
        String setKey = "attendees";
        String hashKey = "attendee:info:" + userDetails.getUsername();

        redisTemplate.opsForSet().remove(setKey, userDetails.getUsername());
        redisTemplate.delete(hashKey);
    }

    public void registSocket(CustomUserDetails userDetails) {
        String s = userDetails.getUsername();
    }
}
