package com.sports.match.mypage.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MyInfoDto {
    private int memNo;
    private String memId;
    private String memPw;
    private String memName;
    private String memTel;
    private String memEmail;
    private LocalDateTime createdAt;
    private String memRole;
    private String grade;
    private String approved;
    private Date attDate;
    public String getFormattedTel() {
        if (memTel == null || memTel.length() != 11) return memTel;
        return memTel.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    }
}
