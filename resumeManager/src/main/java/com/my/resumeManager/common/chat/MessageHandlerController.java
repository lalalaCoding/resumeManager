package com.my.resumeManager.common.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MessageHandlerController { //메시지 핸들러 (가공 목적)
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	
	@MessageMapping("/hello") //요청 메시지의 destination 헤더 경로
	@SendTo("/topic/greetings") //반환값을 메시지로 전달할 경로 지정
	public void greeting() {
		log.info("greeting");
		
		String message = "안녕하세요";
		messagingTemplate.convertAndSend("/topic/greetings", message);
	}
	
}
