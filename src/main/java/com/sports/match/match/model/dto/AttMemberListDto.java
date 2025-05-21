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

    public void setGrade(String grade) {
        switch (grade.toLowerCase()) {
            case "low":
                this.grade = "초급";
                break;
            case "middle":
                this.grade = "중급";
                break;
            case "high":
                this.grade = "상급";
                break;
            default:
                this.grade = grade; // 예외 처리: 변환 안 되는 값은 그대로 저장
        }
    }
}
