package com.sports.match.auth.model.mapper;

import com.sports.match.auth.model.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

@Mapper
public interface AuthMapper {
    MemberDto selectMemberById(String memId);

    int attend(@Param("memNo") int memNo, @Param("attDate") LocalDate attDate);

    int getAttend(@Param("memNo") int memNo, @Param("attDate") LocalDate attDate);

    int attendCancel(@Param("memNo") int memNo, @Param("attDate") LocalDate attDate);
}
