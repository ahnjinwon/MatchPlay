package com.sports.match.match.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueueDto {
    private int courtId;
    private List<MemberDto> teamA;
    private List<MemberDto> teamB;

    @Data
    public static class MemberDto {
        private String memId;
        private String memName;
    }
}
