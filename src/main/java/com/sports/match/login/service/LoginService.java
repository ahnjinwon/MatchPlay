package com.sports.match.login.service;

import com.sports.match.login.model.dao.LoginDao;
import com.sports.match.login.model.dto.RegistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final LoginDao loginDao;

    public void registMember(RegistDto registDto) {
        loginDao.registMember(registDto);
    }

    public boolean checkById(String memId) {
        return loginDao.checkById(memId) > 0;
    }
}
