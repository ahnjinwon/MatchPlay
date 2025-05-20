package com.sports.match.match.model.dao;

import com.sports.match.match.model.dto.AttMemberListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchDao {
    List<AttMemberListDto> getAttMemList(String today);
}
