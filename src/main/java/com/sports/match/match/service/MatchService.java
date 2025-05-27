package com.sports.match.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.match.match.model.dao.MatchDao;
import com.sports.match.match.model.dto.AttMemberListDto;
import com.sports.match.match.model.dto.QueueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchDao matchDao;
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public List<AttMemberListDto> getAttMemList() {
        String setKey = "attendees";

        Set<String> memIds = redisTemplate.opsForSet().members(setKey);
        List<AttMemberListDto> attMemList = new ArrayList<>();

        if (memIds != null) {
            for (String Id : memIds) {
                String hashKeys = "attendee:info:" + Id;
                Map<Object, Object> info = redisTemplate.opsForHash().entries(hashKeys);
                AttMemberListDto dto = new AttMemberListDto();
                dto.setMemId(Id);
                dto.setMemName((String) info.get("memName"));
                dto.setGrade((String) info.get("grade"));
                dto.setStatus((String) info.get("status"));

                attMemList.add(dto);
            }
            attMemList.sort(Comparator.comparing(AttMemberListDto::getMemName));
        }
        System.out.println(attMemList);
        return attMemList;
    }

    public ResponseEntity<?> saveQueue(QueueDto queueDto) {
        try {
            String key = "queue:court:" + queueDto.getCourtId();

            // Redis에 JSON 저장 (memName 포함해도 무방)
            String value = objectMapper.writeValueAsString(Map.of(
                    "teamA", queueDto.getTeamA(),
                    "teamB", queueDto.getTeamB()
            ));
            redisTemplate.opsForList().rightPush(key, value);
            LocalDateTime midnight = LocalDateTime.now().plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            Date expireAt = Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());

            // 만료 시간 설정
            redisTemplate.expireAt(key, expireAt);

            // status 업데이트
            List<QueueDto.MemberDto> allMembers = new ArrayList<>();
            allMembers.addAll(queueDto.getTeamA());
            allMembers.addAll(queueDto.getTeamB());

            for (QueueDto.MemberDto member : allMembers) {
                String memberKey = "attendee:info:" + member.getMemId();
                redisTemplate.opsForHash().put(memberKey, "status", "1");
            }

            return ResponseEntity.ok(Map.of("message", "대기열 저장 완료"));
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("JSON 변환 실패: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("대기열 저장 실패: " + e.getMessage());
        }
    }

    public ResponseEntity<?> queueList(int courtId) {
        String key = "queue:court:" + courtId;
        try {
            List<String> rawList = redisTemplate.opsForList().range(key, 0, -1);
            if (rawList == null) return ResponseEntity.ok(Collections.emptyList());

            List<Map<String, List<Map<String, String>>>> parsedQueue = new ArrayList<>();

            for (String item : rawList) {
                Map<String, List<Map<String, String>>> parsedItem = objectMapper.readValue(
                        item, new TypeReference<>() {});
                parsedQueue.add(parsedItem);
            }

            return ResponseEntity.ok(parsedQueue);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("대기열 불러오기 실패: " + e.getMessage());
        }
    }
}
