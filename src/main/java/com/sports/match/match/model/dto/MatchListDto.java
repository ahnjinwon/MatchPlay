package com.sports.match.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchListDto {
    private int courtId;
    private String memId;
    private String memName;
    private Integer score = 0;
}
