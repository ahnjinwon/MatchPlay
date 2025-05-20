package com.sports.match.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttMemberListDto {
    private String memId;
    private String memName;
    private String grade;
}
