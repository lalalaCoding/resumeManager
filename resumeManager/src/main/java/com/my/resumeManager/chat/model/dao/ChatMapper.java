package com.my.resumeManager.chat.model.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.my.resumeManager.chat.model.vo.ChatMember;
import com.my.resumeManager.chat.model.vo.ChatMessage;
import com.my.resumeManager.chat.model.vo.ChatRoom;

@Mapper
public interface ChatMapper {

	ChatRoom myChatMember(@Param("visiterMap") HashMap<String, Integer> visiterMap);

	int insertChatRoom(ChatRoom newRoom);

	int insertChatMember(@Param("visiterMap") HashMap<String, Integer> visiterMap);

	ArrayList<ChatMember> selectChatMemberList(@Param("conditionMap") Map<String, Object> conditionMap);

	ArrayList<ChatMessage> selectAllChatMessageList(ArrayList<ChatMember> myChatMember);

	int insertChatMessage(ChatMessage chat);

	ArrayList<ChatMessage> selectChatMessageList(@Param("myRoomNoSet") Set<Integer> myRoomNoSet);

	
	
	
	
	
}
