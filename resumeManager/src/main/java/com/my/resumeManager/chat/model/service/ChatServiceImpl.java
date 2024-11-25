package com.my.resumeManager.chat.model.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.chat.model.dao.ChatMapper;
import com.my.resumeManager.chat.model.vo.ChatException;
import com.my.resumeManager.chat.model.vo.ChatRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	private ChatMapper cMapper;

	@Override
	public ChatRoom myChatMember(HashMap<String, Integer> visiterMap) {
		ChatRoom myRoom = cMapper.myChatMember(visiterMap);
		log.info("myRoom={}", myRoom);
		
		if(myRoom == null) { //채팅방이 존재하지 않는 경우
			//방을 개설 -> 해당 방번호를 가지고 참여 멤버 생성
			ChatRoom newRoom = new ChatRoom();
			int roomResult = cMapper.insertChatRoom(newRoom); //방을 개설하면서 생성된 방 번호가 newRoom의 필드에 설정된다.
			if (roomResult > 0) {
				//해당 방에 참여하는 멤버를 생성한다.
				visiterMap.put("roomNo", newRoom.getRoomNo());
				int memResult = cMapper.insertChatMember(visiterMap);
				if (memResult == 2) {
					
					
					
					
					
				} else {
					new ChatException("서비스 요청 실패");
				}
			} else {
				new ChatException("서비스 요청 실패");
			}
		}
		
		return myRoom;
	}
	
	
	
	
	
	
	
	
}
