package com.my.resumeManager.common.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.my.resumeManager.chat.model.service.ChatService;
import com.my.resumeManager.chat.model.vo.ChatMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MessageHandlerController { //메시지 핸들러 (가공 목적)
	
	@Autowired
    private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
	private ChatService cService;
	
	
	@MessageMapping("/{roomNo}") //요청 메시지의 destination 헤더 경로
	@SendTo("/queue/{roomNo}") //반환값을 메시지로 전달할 경로 지정
	public void chat(@DestinationVariable("roomNo") int roomNo, ChatMessage chat) {
		log.info("roomNo={}", roomNo);
		log.info("ChatMessage={}", chat);
		
		//채팅 내용 저장 요청
		//messageNo=0, messageContent=123, messageCount=0, messageCreate=null, messageStatus= ,, roomVisiterId=test010, joinNo=21
		int result = cService.saveChatMessage(chat);
		if (result > 0) { //저장 성공
			messagingTemplate.convertAndSend("/queue/" + roomNo, chat);
		} else {
			//저장 실패
			chat.setMessageContent("전송이 실패하였습니다.");
			messagingTemplate.convertAndSend("/queue/" + roomNo, chat);
		}
		
		
	}
	
}
