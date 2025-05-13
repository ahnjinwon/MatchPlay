package com.sports.match.mypage.model.dao;

import com.sports.match.mypage.model.dto.MyInfoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MyPageDao {
    MyInfoDto getMyInfo(int memNo);

    int updateProfile(MyInfoDto myInfoDto);

    void changePw(@Param("memPw") String memPw, @Param("memNo") int memNo);
}
