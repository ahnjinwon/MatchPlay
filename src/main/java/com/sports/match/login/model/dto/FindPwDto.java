package com.sports.match.login.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPwDto {
    private String memEmail;
    private String memName;
    private String memId;
    private String memPw;
}
