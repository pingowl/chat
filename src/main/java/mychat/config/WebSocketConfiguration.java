package mychat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker // WebSocket을 활성화하고 메시지 브로커 사용가능
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // STOMP 엔드포인트를 등록하는 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // ws://localhost:8080/ws-chat 으로 요청
        registry.addEndpoint("/ws-chat") // STOMP 엔드포인트 설정
                .setAllowedOriginPatterns("*") // 모든 Origin 허용 -> 배포시에는 보안을 위해 Origin을 정확히 지정
                .withSockJS(); // SockJS 사용가능 설정 -> Stomp를 지원하지 않는 브라우저나 환경에서 사용할경우 대비
    }

    // 메시지 브로커를 구성하는 메서드
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url
        registry.enableSimpleBroker("/sub"); // /sub/room/{roomNo}로 주제 구독 가능
        // 메시지를 발행하는 요청 url
        registry.setApplicationDestinationPrefixes("/pub"); // /pub/room/{roomNo} 로 메시지 전송 컨트롤러 라우팅 가능
    }

    // STOMP에서 64KB 이상의 데이터 전송을 못하는 문제 해결
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) { // 이미지를 문자열로 인코딩한 경우 대비
        registry.setMessageSizeLimit(160 * 64 * 1024);
        registry.setSendTimeLimit(100 * 10000);
        registry.setSendBufferSizeLimit(3 * 512 * 1024);
    }

}
