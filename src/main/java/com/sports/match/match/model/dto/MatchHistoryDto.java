package com.sports.match.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchHistoryDto {
    private int courtId;
    private int matchId;
    private String memId;
    private String team;
    private String memName;
    private Integer score = 0;
}
