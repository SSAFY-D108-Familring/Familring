package com.familring.familyservice.config.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // 메시지 브로커 구성 설정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트로부터의 메시지를 브로커가 받아서 전달할 때 사용할 경로를 설정
        // "/topic"이 prefix로 설정된 경로로 클라이언트가 메시지를 구독할 수 있음
        config.enableSimpleBroker("/topic");

        // 클라이언트가 서버로 메시지를 보낼 때 사용할 prefix 설정
        // 클라이언트는 "/app"이 prefix로 설정된 경로로 메시지를 전송하게 됨
        config.setApplicationDestinationPrefixes("/app");
    }

    // STOMP 엔드포인트 등록
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") // "/ws-chat" 엔드포인트를 등록하여 클라이언트가 WebSocket을 통해 연결할 수 있도록 함
                .setAllowedOriginPatterns("*") // CORS 설정으로, 허용된 도메인에서의 요청만 받아들임
                .withSockJS(); // SockJS를 활성화하여 WebSocket을 지원하지 않는 브라우저에서도 동작하도록 함
    }
}
