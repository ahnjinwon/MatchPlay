package com.sports.match.main.service;

import com.sports.match.main.model.dao.testDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class testService {
    @Autowired
    private testDao testdao;
    public int test(){
        int num=testdao.getNum();
        return num;
    }
}
