package com.sports.match.login.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistDto {
    private String memId;
    private String memPw;
    private String memName;
    private String memTel;
    private String grade;
}
