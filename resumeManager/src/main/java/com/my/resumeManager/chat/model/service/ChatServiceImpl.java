package com.my.resumeManager.chat.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.chat.model.dao.ChatMapper;
import com.my.resumeManager.chat.model.vo.ChatException;
import com.my.resumeManager.chat.model.vo.ChatMember;
import com.my.resumeManager.chat.model.vo.ChatMessage;
import com.my.resumeManager.chat.model.vo.ChatRoom;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService{
	
	@Autowired
	private ChatMapper cMapper;

	@Override
	public ArrayList<ChatMember> myChatMember(HashMap<String, Integer> visiterMap) {
		//나의 CHAT_MEMBER 정보 + 상대방의 CHAT_MEMBER 정보를 조회해야 함
		int myRoomNo = 0;
		ChatRoom myRoom = cMapper.myChatMember(visiterMap);
		log.info("myRoom={}", myRoom);
		
		if(myRoom == null) { //채팅방이 존재하지 않는 경우
			log.info("방 개설 로직 실행");
			//방을 개설 -> 해당 방번호를 가지고 참여 멤버 생성
			ChatRoom newRoom = new ChatRoom();
			int roomResult = cMapper.insertChatRoom(newRoom); //방을 개설하면서 생성된 방 번호가 newRoom의 필드에 설정된다.
			if (roomResult > 0) {
				//해당 방에 참여하는 멤버를 생성한다.
				visiterMap.put("roomNo", newRoom.getRoomNo());
				int memResult = cMapper.insertChatMember(visiterMap);
				if (memResult == 2) {
					//채팅방 생성이 성공
					myRoomNo = newRoom.getRoomNo();
				} else {
					new ChatException("서비스 요청 실패");
				}
			} else {
				new ChatException("서비스 요청 실패");
			}
		} else {
			myRoomNo = myRoom.getRoomNo();
		}
		
		Map<String, Object> conditionMap = new HashMap<>();
		conditionMap.put("condition", "roomNo");
		conditionMap.put("roomNo", myRoomNo);
		ArrayList<ChatMember> memList = cMapper.selectChatMemberList(conditionMap);
		log.info("memList={}", memList);
		
		return memList;
	}

	@Override
	public ArrayList<ChatMessage> myMessageList(ArrayList<ChatMember> myChatMember) {
		return cMapper.selectAllChatMessageList(myChatMember);
	}

	@Override
	public ArrayList<ChatMember> myChatMemberList(int memberNo) {
		Map<String, Object> conditionMap = new HashMap<>();
		conditionMap.put("condition", "memberNo");
		conditionMap.put("memberNo", memberNo);
		return cMapper.selectChatMemberList(conditionMap);
	}

	@Override
	public int saveChatMessage(ChatMessage chat) {
		return cMapper.insertChatMessage(chat);
	}

	@Override
	public ArrayList<ChatMessage> myRecentChatMessageList(Set<Integer> myRoomNoSet) {
		//최근 메시지가 없는 경우 : 기본 메시지를 등록해주자!
		ArrayList<ChatMessage> recentList = cMapper.selectChatMessageList(myRoomNoSet);
		log.info("myRoomNoSet={}",myRoomNoSet);
		log.info("recentList={}",recentList);
		
		for (ChatMessage cm : recentList) {
			myRoomNoSet.remove(cm.getRoomNo()); //최근 메시지가 존재하는 방 번호를 제외시킨다.
		}
		
		log.info("myRoomNoSet={}",myRoomNoSet);
		
		for (Integer roomNo : myRoomNoSet) {
			ChatMessage defaultChatMessage = new ChatMessage();
			defaultChatMessage.setRoomNo((int) roomNo);
			defaultChatMessage.setMessageContent("대화 내역이 없습니다.");
			recentList.add(defaultChatMessage);
		}
		
		return recentList;
	}
	
	
	
	
	
	
	
	
}
