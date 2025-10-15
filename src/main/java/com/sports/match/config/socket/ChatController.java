package com.sports.match.config.socket;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Data
class ChatMessage {
    private String roomId;     // 방 ID (그룹채팅)
    private Integer toMemNo;   // DM 대상 (없으면 방 메시지)
    private String content;
    private long ts;
}

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final PushService push;

    // 방 채팅
    @MessageMapping("/chat.send") // 클라 → /app/chat.send
    public void sendToRoom(@Payload ChatMessage msg, Principal principal) {
        int from = Integer.parseInt(principal.getName()); // 로그인한 사용자 memNo
        msg.setTs(System.currentTimeMillis());
        push.sendToRoom(msg.getRoomId(), msg);
    }

    // 1:1 DM
    @MessageMapping("/chat.pm") // 클라 → /app/chat.pm
    public void sendPm(@Payload ChatMessage msg, Principal principal) {
        int from = Integer.parseInt(principal.getName());
        msg.setTs(System.currentTimeMillis());

        // 본인 에코
        push.sendDirect(from, msg);
        // 상대방 전송
        push.sendDirect(msg.getToMemNo(), msg);
    }
}
