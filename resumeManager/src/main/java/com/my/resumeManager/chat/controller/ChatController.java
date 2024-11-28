package com.my.resumeManager.chat.controller;


import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.resumeManager.chat.model.service.ChatService;
import com.my.resumeManager.chat.model.vo.ChatException;
import com.my.resumeManager.chat.model.vo.ChatMember;
import com.my.resumeManager.chat.model.vo.ChatMessage;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.member.model.vo.MemberException;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ChatController {
	
	@Autowired
	private ChatService cService;
	
	
	@GetMapping("chats/{memberNo}") //나의 채팅 목록 조회
	public String chatListPage(@PathVariable("memberNo") int memberNo, HttpSession session, Model model) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/loginPage.me";
		} else if (memberNo != loginMember.getMemberNo()) {
			throw new MemberException("서비스 요청 실패");
		} else {
			//나의 채팅 목록 조회
			
			
			
			
			
			model.addAttribute("info", "chat");
			return "chat/chatList";
		}
	}
	
	@GetMapping("chats/{memberNo}/room") //채팅 방 조회(들어가기)
	public String chatRoomPage(@PathVariable("memberNo") int senderNo, @RequestParam("receiver") int receiverNo, HttpSession session, Model model) {
		log.info("채팅 발신자 번호={}", senderNo);
		log.info("채팅 수신자 번호={}", receiverNo);
//		채팅 방이 존재 -> 채팅 방 조회
//		채팅 방이 없음 -> 채팅 방 생성 + 채팅 방 조회
		
		//나와 상대방이 참여 중인 CHAT_MEMBER 데이터 조회하기
		HashMap<String, Integer> visiterMap = new HashMap<>();
		visiterMap.put("senderNo", senderNo);
		visiterMap.put("receiverNo", receiverNo);
		ArrayList<ChatMember> myChatMember = cService.myChatMember(visiterMap);
		if (myChatMember.size() > 2) { //한 채팅방에 참여중인 회원이 3명 이상이 조회된 경우
			throw new ChatException("서비스 요청 실패");
		}
		log.info("myChatMember={}", myChatMember); //myRoom=[ChatMember(joinNo=21, roomVisiter=10), ChatMember(joinNo=22, roomVisiter=1)]
		
		//대화내역 조회하기
		ArrayList<ChatMessage> myMessageList = cService.myMessageList(myChatMember);
		log.info("myMessageList={}", myMessageList);
		
		model.addAttribute("myChatMember", myChatMember);
		model.addAttribute("myMessageList", myMessageList);
		model.addAttribute("info", "chat");
		return "chat/chatRoom";
	}
	
	@PostMapping("chats/{memberNo}/room") //채팅 전송 요청 처리
	@ResponseBody
	public String sendChatting(@PathVariable("memberNo") int senderNo, @ModelAttribute("ChatMessage") ChatMessage msg, HttpSession session) {
		//[messageNo=0, messageContent=123, messageCount=0, messageCreate=null, messageStatus=
		log.info("msg={}", msg);
		
		
		
		
		
		
		return null;
	}
	
	
	
}
