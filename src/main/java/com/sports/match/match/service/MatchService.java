package com.sports.match.match.service;

import com.sports.match.match.model.dao.MatchDao;
import com.sports.match.match.model.dto.AttMemberListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchDao matchDao;
    private final RedisTemplate<String, String> redisTemplate;

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

                attMemList.add(dto);
            }
            attMemList.sort(Comparator.comparing(AttMemberListDto::getMemName));
        }

        return attMemList;
    }
}
