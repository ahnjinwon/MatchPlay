package com.sports.match.mypage.service;

import com.sports.match.config.security.CustomUserDetails;
import com.sports.match.mypage.model.dao.MyPageDao;
import com.sports.match.mypage.model.dto.MyInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageDao myPageDao;

    public MyInfoDto getMyInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return myPageDao.getMyInfo(userDetails.getMemNo());
    }

    public int updateProfile(MyInfoDto myInfoDto) {
        int result = myPageDao.updateProfile(myInfoDto);
        return result;
    }
}
