package com.sports.match.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class attDto {
    private int attId;
    private String memNo;
    private Date attDate;
    private Timestamp createdAt;
}
