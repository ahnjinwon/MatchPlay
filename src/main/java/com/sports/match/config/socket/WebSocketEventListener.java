package com.sports.match.config.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

    private final SimpMessagingTemplate template;

    @EventListener
    public void handleSubscribe(org.springframework.web.socket.messaging.SessionSubscribeEvent event) {
        var sha = StompHeaderAccessor.wrap(event.getMessage());
        String dest = sha.getDestination();              // 예: "/user/queue/notice"
        var user = sha.getUser();

        if (user != null && dest != null && dest.endsWith("/queue/notice")) {
            String username = user.getName();            // 예: "test3"
            // 구독이 확인된 후에 전송 → 레이스 없음
            template.convertAndSendToUser(username, "/queue/notice", "연결되었습니다 ✅");
        }
    }
}
