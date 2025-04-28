package com.sports.match.mypage.model.dao;

import com.sports.match.mypage.model.dto.MyInfoDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MyPageDao {
    MyInfoDto getMyInfo(int memNo);

    int updateProfile(MyInfoDto myInfoDto);
}
