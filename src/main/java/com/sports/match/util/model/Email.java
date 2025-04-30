package com.sports.match.util.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Email {
    private String receiveAddress;
    private String mailTitle;
    private String mailContent;
}
