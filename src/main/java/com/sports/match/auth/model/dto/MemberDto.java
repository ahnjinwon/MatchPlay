package com.sports.match.auth.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@RequiredArgsConstructor
@Data
public class MemberDto {
    private String memNo;
    private String memId;
    private String memPw;
    private String memName;
    private String memTel;
    private String memEmail;
    private Timestamp createdAt;
    private String memRole;
    private String grade;
    private String approved;
}
