package com.sports.match.auth.model.mapper;

import com.sports.match.auth.model.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    MemberDto selectMemberById(String memId);
}
