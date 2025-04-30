package com.sports.match.util;

import com.sports.match.util.model.Email;
import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class EmailUtil {
    private final JavaMailSender javaMailSender;

    public void sendEmail(Email email) throws Exception { // 이메일 전송 클래스
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getReceiveAddress());
        message.setSubject(email.getMailTitle());
        message.setText(email.getMailContent());
        message.setFrom("MatchPlay <sudden3415@gmail.com>");
        message.setReplyTo("sudden3415@gmail.com");
        System.out.println(email.toString());
        javaMailSender.send(message);
    }
}