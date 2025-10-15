package com.sports.match.config.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PushService {
    private final org.springframework.messaging.simp.SimpMessagingTemplate template;

    // memNo 기준 (String으로 보내야 함)
    public void notifyUser(int memNo, Object payload) {
        template.convertAndSendToUser(String.valueOf(memNo), "/queue/notice", payload);
    }

    public void sendToRoom(String roomId, Object payload) {
        template.convertAndSend("/topic/room." + roomId, payload);
    }

    public void sendDirect(int toMemNo, Object payload) {
        template.convertAndSendToUser(String.valueOf(toMemNo), "/queue/pm", payload);
    }
}