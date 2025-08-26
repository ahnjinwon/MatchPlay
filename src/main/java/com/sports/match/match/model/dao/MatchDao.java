package com.sports.match.match.model.dao;

import com.sports.match.match.model.dto.AttMemberListDto;
import com.sports.match.match.model.dto.MatchHistoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MatchDao {
    List<AttMemberListDto> getAttMemList(String today);



    void insertMatchHistoryList(List<MatchHistoryDto> dtoList);

    void getMatchId(Map<String, Object> map);
}
