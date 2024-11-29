package com.my.resumeManager.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //스프링에서 메시지 브로커 기능 활성화
public class WebSocketBrokerConfig  implements WebSocketMessageBrokerConfigurer{ //웹소켓 메시지 브로커 설정

	@Override //클라이언트가 웹소켓 연결을 초괴화할 수 있는 엔드포인트 등록
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/chattings") //웹소켓 연결 시 요청을 보낼 EndPoint 지정
					.withSockJS();  //SockJS 라이브러리 사용
	}

	@Override //메시지 브로커 동작을 구성
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/queue", "/topic"); //queue는 1:1 관계에서, topic은 1:N 관계에서 사용하는 것이 관습이다.
		registry.setApplicationDestinationPrefixes("/app"); //메시지 가공이 필요한 경우 메시지 핸들러로 라우팅시켜주는 prefix를 지정
	} 
}
