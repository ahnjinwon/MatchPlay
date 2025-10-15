package com.sports.match.config.socket;

import com.sports.match.config.security.CustomUserDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setHandshakeHandler(new AuthHandshakeHandler()) // Principal = CustomUserDetails
                .setAllowedOriginPatterns("*")
                .withSockJS(); // 필요 시 제거 가능
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }
}

class AuthHandshakeHandler extends org.springframework.web.socket.server.support.DefaultHandshakeHandler {
    @Override
    protected Principal determineUser(ServerHttpRequest req, WebSocketHandler wsHandler, Map<String, Object> attrs) {
        var ctx = org.springframework.security.core.context.SecurityContextHolder.getContext();
        var auth = ctx.getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails cud) {
            return cud; // getName() == String.valueOf(memNo)
        }
        return super.determineUser(req, wsHandler, attrs);
    }
}