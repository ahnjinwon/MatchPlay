package com.sports.match.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sports.match.match.model.dao.MatchDao;
import com.sports.match.match.model.dto.AttMemberListDto;
import com.sports.match.match.model.dto.MatchDto;
import com.sports.match.match.model.dto.MatchListDto;
import com.sports.match.match.model.dto.QueueDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

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
            String match = "match:court:"+queueDto.getCourtId();

            //대기열이 비어있으면 매치로 바로 이동
            List<String> rawList = redisTemplate.opsForList().range(match, 0, -1);

            if (rawList.isEmpty()){
                // Redis에 JSON 저장
                String value = objectMapper.writeValueAsString(Map.of(
                        "teamA", queueDto.getTeamA().stream()
                                .map(qm -> {
                                    MatchDto.MemberDto mm = new MatchDto.MemberDto();
                                    mm.setMemId(qm.getMemId());
                                    mm.setMemName(qm.getMemName());
                                    mm.setScore(0);
                                    return mm;
                                })
                                .collect(Collectors.toList()),
                        "teamB", queueDto.getTeamB().stream()
                                .map(qm -> {
                                    MatchDto.MemberDto mm = new MatchDto.MemberDto();
                                    mm.setMemId(qm.getMemId());
                                    mm.setMemName(qm.getMemName());
                                    mm.setScore(0);
                                    return mm;
                                })
                                .collect(Collectors.toList())
                ));
                redisTemplate.opsForList().rightPush(match, value);
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
                    redisTemplate.opsForHash().put(memberKey, "status", "2");
                }

                return ResponseEntity.ok(Map.of("message", "현재 진행중인 매치가 없으므로 바로 매치로 이동합니다."));
            }
            else{
                // Redis에 JSON 저장
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

                return ResponseEntity.ok(Map.of("message", "대기열에 등록되었습니다."));
            }
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
    //여기에 match 정보 담아서 보내주면 받아갈거임
    public List<MatchListDto> getMatchMember(int courtId) {
        String key = "match:court:" + courtId;
        List<MatchListDto> noList = new ArrayList<>();
        try {
            List<String> rawList = redisTemplate.opsForList().range(key, 0, -1);
            if (rawList == null){
                List<MatchListDto> list = new ArrayList<>();
                for(int j=0; j<4; j++){
                    MatchListDto matchListDto = new MatchListDto();
                    matchListDto.setCourtId(courtId);
                    matchListDto.setMemName("");
                    list.add(matchListDto);
                }
                return list;
            }else{
                //여기부터 수정!
                //list로 보낼거임
                List<MatchListDto> list = new ArrayList<>();

                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return noList;
    }
}
