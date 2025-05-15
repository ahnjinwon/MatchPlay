package com.sports.match.login.model.dao;

import com.sports.match.login.model.dto.RegistDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface LoginDao {
    void registMember(RegistDto registDto);

    int checkById(String memId);

    String checkEmail(@Param("memEmail") String memEmail,@Param("memName") String memName);
}
