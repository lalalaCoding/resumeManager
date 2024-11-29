package com.my.resumeManager.common.chat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class MessageHandlerController { //메시지 핸들러 (가공 목적)
	
	@MessageMapping("/hello") //요청 메시지의 destination 헤더 경로
	@SendTo("/topic/greeting") //반환값을 메시지로 전달할 경로 지정
	public String greeting() {
		
		
		
		
		return null;
	}
	
}
