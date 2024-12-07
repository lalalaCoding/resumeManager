package com.my.resumeManager.chat.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.my.resumeManager.chat.model.vo.ChatMember;
import com.my.resumeManager.chat.model.vo.ChatMessage;

public interface ChatService {

	ArrayList<ChatMember> myChatMember(HashMap<String, Integer> visiterMap);

	ArrayList<ChatMessage> myMessageList(ArrayList<ChatMember> myChatMember);

	ArrayList<ChatMember> myChatMemberList(int memberNo);

	int saveChatMessage(ChatMessage chat);

	ArrayList<ChatMessage> myRecentChatMessageList(Set<Integer> myRoomNoSet);

}
