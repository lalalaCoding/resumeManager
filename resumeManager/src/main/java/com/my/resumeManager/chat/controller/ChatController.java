package com.my.resumeManager.chat.controller;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.resumeManager.chat.model.service.ChatService;
import com.my.resumeManager.chat.model.vo.ChatMember;
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
		
		//내가 참여하고 있는 방 조회
		ArrayList<ChatMember> list;
		
		
		
		
		
		model.addAttribute("info", "chat");
		return "chat/chatRoom";
	}
	
	
	
	
	
	
}
