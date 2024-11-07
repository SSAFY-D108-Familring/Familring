package com.familring.familyservice.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 클라이언트에서 메시지를 전송할 때 사용하는 경로
        registry.setApplicationDestinationPrefixes("/send");
        // 구독용 경로 설정
        registry.enableSimpleBroker("/room");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // StompHandler로 메시지 가로채기
        registration.interceptors(stompHandler);
    }
}