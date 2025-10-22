package com.sports.match.chat.model.dao;

import com.sports.match.chat.model.dto.userInfoDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface ChattingDao {
    ArrayList<userInfoDto> getuserInfo();
}
