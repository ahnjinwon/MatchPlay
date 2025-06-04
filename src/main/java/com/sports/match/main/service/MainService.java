package com.sports.match.main.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MainService {

    private final RedisTemplate<String, String> redisTemplate;
    public void resetCourt() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        //매치 초기화
        for (int courtId = 1; courtId <= 3; courtId++) {
            String key = "match:court:" + courtId;

            // 1) 대기열 리스트 가져오기 (List<String> JSON 문자열)
            List<String> rawList = redisTemplate.opsForList().range(key, 0, -1);
            if (rawList != null) {
                for (String item : rawList) {
                    Map<String, Object> parsedItem = objectMapper.readValue(item, new TypeReference<>() {});

                    List<Map<String, String>> teamA = (List<Map<String, String>>) parsedItem.get("teamA");
                    List<Map<String, String>> teamB = (List<Map<String, String>>) parsedItem.get("teamB");

                    Stream.concat(teamA.stream(), teamB.stream())
                            .forEach(player -> {
                                String memId = player.get("memId");
                                if (memId != null) {
                                    String hashKey = "attendee:info:" + memId;
                                    redisTemplate.opsForHash().put(hashKey, "status", "0");
                                }
                            });
                }
            }
            redisTemplate.delete(key);
        }
        //대기열 초기화
        for (int courtId = 1; courtId <= 3; courtId++) {
            String key = "queue:court:" + courtId;

            // 1) 대기열 리스트 가져오기 (List<String> JSON 문자열)
            List<String> rawList = redisTemplate.opsForList().range(key, 0, -1);
            if (rawList != null) {
                for (String item : rawList) {
                    // JSON 문자열을 Map<String, List<Map<String, String>>> 으로 파싱
                    Map<String, List<Map<String, String>>> parsedItem = objectMapper.readValue(
                            item, new TypeReference<>() {}
                    );

                    // teamA, teamB에 있는 모든 참가자 memId 뽑기
                    List<Map<String, String>> teamA = parsedItem.get("teamA");
                    List<Map<String, String>> teamB = parsedItem.get("teamB");

                    Stream.concat(teamA.stream(), teamB.stream())
                            .forEach(player -> {
                                String memId = player.get("memId");
                                if (memId != null) {
                                    // status 0으로 업데이트
                                    String hashKey = "attendee:info:" + memId;
                                    redisTemplate.opsForHash().put(hashKey, "status", "0");
                                }
                            });
                }
            }

            // 2) 대기열 리스트 삭제 (초기화)
            redisTemplate.delete(key);
        }
    }
}
