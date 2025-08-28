package com.sports.match.match.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sports.match.match.model.dao.MatchDao;
import com.sports.match.match.model.dto.*;
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
                redisTemplate.expireAt(match, expireAt);

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

    public List<MatchListDto> getMatchMember(int courtId) {
        String key = "match:court:" + courtId;
        List<MatchListDto> noList = new ArrayList<>();
        try {
            List<String> rawList = redisTemplate.opsForList().range(key, 0, 0);
            if (rawList == null || rawList.isEmpty()){
                List<MatchListDto> list = new ArrayList<>();
                for(int j=0; j<4; j++){
                    MatchListDto matchListDto = new MatchListDto();
                    matchListDto.setCourtId(courtId);
                    matchListDto.setMemName("None");
                    list.add(matchListDto);
                }
                return list;
            }else{
                List<MatchListDto> list = new ArrayList<>();
                Map<String, List<Map<String, Object>>> map = objectMapper.readValue(rawList.get(0),
                        new TypeReference<>() {});

                List<Map<String, Object>> teamA = map.get("teamA");
                List<Map<String, Object>> teamB = map.get("teamB");

                if (teamA != null) {
                    for (Map<String, Object> member : teamA) {
                        MatchListDto dto = new MatchListDto();
                        dto.setCourtId(courtId);
                        dto.setMemId((String) member.get("memId"));
                        dto.setMemName((String) member.get("memName"));
                        dto.setScore((Integer) member.getOrDefault("score", 0));
                        list.add(dto);
                    }
                }

                if (teamB != null) {
                    for (Map<String, Object> member : teamB) {
                        MatchListDto dto = new MatchListDto();
                        dto.setCourtId(courtId);
                        dto.setMemId((String) member.get("memId"));
                        dto.setMemName((String) member.get("memName"));
                        dto.setScore((Integer) member.getOrDefault("score", 0));
                        list.add(dto);
                    }
                }

                return list;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return noList;
    }

    public int scorePlus(ScoreDto scoreDto) {
        String key = "match:court:" + scoreDto.getCourtId();
        List<String> jsonList = redisTemplate.opsForList().range(key, 0, -1);
        int score = 0;
        if (jsonList == null) {
            // 매치 데이터가 없으면 처리
            return 99;
        }
        String json = jsonList.get(0);
        try{
            Map<String, List<MatchDto.MemberDto>> matchMap = objectMapper.readValue(
                    json,
                    new TypeReference<Map<String, List<MatchDto.MemberDto>>>() {}
            );

            boolean updated = false;
            for (String team : List.of("teamA", "teamB")) {
                List<MatchDto.MemberDto> members = matchMap.get(team);
                if (members != null) {
                    for (MatchDto.MemberDto member : members) {
                        if (member.getMemId().equals(scoreDto.getMemId())) {
                            score = member.getScore() + 1;
                            member.setScore(score);
                            updated = true;
                            break;
                        }
                    }
                }
                if (updated) break;
            }

            if (updated) {
                String updatedJson = objectMapper.writeValueAsString(matchMap);
                redisTemplate.opsForList().set(key, 0, updatedJson);
            }else{
                return 98;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return score;
    }

    public int scoreMinus(ScoreDto scoreDto) {
        String key = "match:court:" + scoreDto.getCourtId();
        List<String> jsonList = redisTemplate.opsForList().range(key, 0, -1);
        int score = 0;
        if (jsonList == null) {
            // 매치 데이터가 없으면 처리
            return 99;
        }
        String json = jsonList.get(0);
        try{
            Map<String, List<MatchDto.MemberDto>> matchMap = objectMapper.readValue(
                    json,
                    new TypeReference<Map<String, List<MatchDto.MemberDto>>>() {}
            );

            boolean updated = false;
            for (String team : List.of("teamA", "teamB")) {
                List<MatchDto.MemberDto> members = matchMap.get(team);
                if (members != null) {
                    for (MatchDto.MemberDto member : members) {
                        if (member.getMemId().equals(scoreDto.getMemId())) {
                            if(member.getScore()>0){
                                score = member.getScore() - 1;
                                member.setScore(score);
                                updated = true;
                                break;
                            }
                            else {
                                return -1;
                            }
                        }
                    }
                }
                if (updated) break;
            }

            if (updated) {
                String updatedJson = objectMapper.writeValueAsString(matchMap);
                redisTemplate.opsForList().set(key, 0, updatedJson);
            }else{
                return 98;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return score;
    }

    public int matchend(int courtId) {
        Map<String, Object> map = new HashMap<>();
        matchDao.getMatchId(map);
        int matchId = (int) map.get("matchId");
        int a_score = 0, b_score = 0;

        String key = "queue:court:" + courtId;
        String match = "match:court:" + courtId;
        List<String> jsonList = redisTemplate.opsForList().range(match, 0, -1);
        List<MatchHistoryDto> dtoList = new ArrayList<>();

        try{
            for (String json : jsonList) {
                JsonNode root = objectMapper.readTree(json);
                // teamA
                if (root.has("teamA")) {
                    for (JsonNode member : root.get("teamA")) {
                        MatchHistoryDto dto = new MatchHistoryDto();
                        dto.setMatchId(matchId);
                        dto.setCourtId(courtId);
                        dto.setTeam("A");
                        dto.setMemId(member.get("memId").asText());
                        dto.setMemName(member.get("memName").asText());
                        dto.setScore(member.get("score").asInt());
                        dtoList.add(dto);
                        a_score+=member.get("score").asInt();
                    }
                }

                // teamB
                if (root.has("teamB")) {
                    for (JsonNode member : root.get("teamB")) {
                        MatchHistoryDto dto = new MatchHistoryDto();
                        dto.setMatchId(matchId);
                        dto.setCourtId(courtId);
                        dto.setTeam("B");
                        dto.setMemId(member.get("memId").asText());
                        dto.setMemName(member.get("memName").asText());
                        dto.setScore(member.get("score").asInt());
                        dtoList.add(dto);
                        b_score+=member.get("score").asInt();
                    }
                }
            }

            String aResult, bResult;
            if (a_score > b_score) {
                aResult = "W";
                bResult = "L";
            } else {
                aResult = "L";
                bResult = "W";
            }

            for (MatchHistoryDto dto : dtoList) {
                if ("A".equals(dto.getTeam())) {
                    dto.setResult(aResult);
                } else {
                    dto.setResult(bResult);
                }
            }

            matchDao.insertMatchHistoryList(dtoList);

            for (MatchHistoryDto dto : dtoList) {
                String memberKey = "attendee:info:" + dto.getMemId();
                redisTemplate.opsForHash().put(memberKey, "status", "0");
            }
            redisTemplate.delete(match);

            String queuedJson = redisTemplate.opsForList().leftPop(key);
            if (queuedJson != null) {
                JsonNode qRoot = objectMapper.readTree(queuedJson);

                ObjectNode nextMatchNode = objectMapper.createObjectNode();
                ArrayNode teamAWithScore = objectMapper.createArrayNode();
                ArrayNode teamBWithScore = objectMapper.createArrayNode();

                // teamA
                if (qRoot.has("teamA")) {
                    for (JsonNode m : qRoot.get("teamA")) {
                        ObjectNode p = objectMapper.createObjectNode();
                        p.put("memId",   m.get("memId").asText());
                        p.put("memName", m.get("memName").asText());
                        p.put("score",   0);
                        teamAWithScore.add(p);
                    }
                }
                // teamB
                if (qRoot.has("teamB")) {
                    for (JsonNode m : qRoot.get("teamB")) {
                        ObjectNode p = objectMapper.createObjectNode();
                        p.put("memId",   m.get("memId").asText());
                        p.put("memName", m.get("memName").asText());
                        p.put("score",   0);
                        teamBWithScore.add(p);
                    }
                }

                nextMatchNode.set("teamA", teamAWithScore);
                nextMatchNode.set("teamB", teamBWithScore);

                String nextMatchJson = objectMapper.writeValueAsString(nextMatchNode);

                redisTemplate.opsForList().leftPush(match, nextMatchJson);

                for (JsonNode p : teamAWithScore) {
                    String memberKey = "attendee:info:" + p.get("memId").asText();
                    redisTemplate.opsForHash().put(memberKey, "status", "1");
                }
                for (JsonNode p : teamBWithScore) {
                    String memberKey = "attendee:info:" + p.get("memId").asText();
                    redisTemplate.opsForHash().put(memberKey, "status", "1");
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return matchId;
    }
}
