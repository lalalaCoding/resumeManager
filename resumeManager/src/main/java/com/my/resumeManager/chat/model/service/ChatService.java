package com.my.resumeManager.chat.model.service;

import java.util.HashMap;

import com.my.resumeManager.chat.model.vo.ChatRoom;

public interface ChatService {

	ChatRoom myChatMember(HashMap<String, Integer> visiterMap);

}
