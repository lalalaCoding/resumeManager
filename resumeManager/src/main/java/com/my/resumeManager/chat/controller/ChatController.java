package com.my.resumeManager.chat.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.my.resumeManager.common.gcs.GCSController;
import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.member.model.vo.MemberException;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
//@RequiredArgsConstructor
public class ChatController {
	
	@Autowired
	private ChatService cService;
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private GCSController gController;
	
	
	@GetMapping("chats/{memberNo}") //나의 채팅 목록 조회
	public String chatListPage(@PathVariable("memberNo") int memberNo, HttpSession session, Model model) {
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/loginPage.me";
		} else if (memberNo != loginMember.getMemberNo()) {
			throw new MemberException("서비스 요청 실패");
		} else {
			//나의 채팅 목록 조회 : 
			ArrayList<ChatMember> myChatList = cService.myChatMemberList(memberNo);
			log.info("myChatList={}", myChatList);
			
			//나의 채팅 대화내역 조회 :
			
			
			
			
			
			
			model.addAttribute("myChatList", myChatList);
			model.addAttribute("info", "chat");
			return "chat/chatList";
		}
	}
	
	@GetMapping("chats/{memberNo}/room") //채팅 방 조회(들어가기)
	public String chatRoomPage(@PathVariable("memberNo") int senderNo, @RequestParam("receiver") int receiverNo, HttpSession session, Model model) {
		log.info("채팅 발신자 번호={}", senderNo);
		log.info("채팅 수신자 번호={}", receiverNo);
		
		Member sender = (Member) session.getAttribute("loginMember");
		if(sender == null) { //로그인되어 있지 않음
			session.setAttribute("msg", "로그인 후 이용해주세요.");
			return "redirect:/loginPage.me";
		} else if(senderNo != sender.getMemberNo()) { //발신자 회원번호와 로그인 회원번호가 일치하지 않음
			throw new MemberException("서비스 요청 실패");
		}
		
		//나와 상대방이 참여 중인 CHAT_MEMBER 데이터 조회하기
		HashMap<String, Integer> visiterMap = new HashMap<>();
		visiterMap.put("senderNo", senderNo);
		visiterMap.put("receiverNo", receiverNo);
		ArrayList<ChatMember> myChatMember = cService.myChatMember(visiterMap);
		if (myChatMember.size() > 2) { //한 채팅방에 참여중인 회원이 3명 이상이 조회된 경우
			throw new ChatException("서비스 요청 실패");
		}
		log.info("myChatMember={}", myChatMember); //[ChatMember(joinNo=21, roomVisiter=10), ChatMember(joinNo=22, roomVisiter=1)]
		
		//수신자 회원정보 조회하기
		Member receiver = mService.selectMemberNo(receiverNo);
		log.info("receiver={}", receiver);
		
		//발신자와 수신자의 프로필 이미지를 로컬에 저장하기
		if (profileLocalDownload(sender, receiver)) {
			//이미지 로컬 저장 성공
			log.info("프로필 로컬 저장 성공");
		} else {
			//이미지 로컬 저장 실패
			throw new ChatException("프로필 이미지 요청 실패");
		}
		
		
		//대화내역 조회하기
		ArrayList<ChatMessage> myMessageList = cService.myMessageList(myChatMember);
		log.info("myMessageList={}", myMessageList);
		
		//참여번호 정리하기
		int senderJoinNo = 0;
		int receiverJoinNo = 0;
		for (ChatMember m : myChatMember) {
			if (m.getRoomVisiter() == sender.getMemberNo()) {
				senderJoinNo = m.getJoinNo();
			} else if (m.getRoomVisiter() == receiver.getMemberNo()) {
				receiverJoinNo = m.getJoinNo();
			} else {
				throw new ChatException("참여 번호를 알 수 없습니다.");
			}
		}
		
		model.addAttribute("myChatMember", myChatMember);
		model.addAttribute("receiver", receiver);
		model.addAttribute("myMessageList", myMessageList);
		model.addAttribute("senderJoinNo", senderJoinNo);
		model.addAttribute("receiverJoinNo", receiverJoinNo);
		model.addAttribute("info", "chat");
		return "chat/chatRoom";
	}
	
	//채팅방 수-발신인의 프로필 로컬 다운로드 메서드
	public boolean profileLocalDownload(Member sender, Member receiver) {
		//destFilePath=C:\resumeManager_downloadFiles, objectName=1731636787493_80976.jpg
		int numberFlag = 0;
		ArrayList<Member> chatVisiters = new ArrayList<>(List.of(sender, receiver)); //ArrayList의 선언과 초기화를 동시에 함
		
		for (Member m : chatVisiters) {
			if (m.getProfileOrigin() != null) { //프로필이 등록된 회원인 경우
				HashMap<String, String> profileMap = new HashMap<>();
				profileMap.put("objectName", m.getProfileRename());
				profileMap.put("destFilePath", m.getProfilePath());
				log.info("프로필 다운로드 정보={}", profileMap);
				
				File myProfile = new File(profileMap.get("destFilePath") + "/" + profileMap.get("objectName"));
				
				if (!myProfile.exists()) { //로컬에 프로필 사진이 저장되어 있지 않음
					gController.objectDownload(profileMap);
				}
				
				if (myProfile.exists()) { //로컬에 파일 다운로드가 성공함
					numberFlag++;
				}
				
			} else {
				numberFlag++; //프로필이 등록되지 않은 회원인 경우
			}
		}
		
		return numberFlag == 2 ? true : false;
	}
	
	
	
	
	@PostMapping("chats/{memberNo}/room") //채팅 전송 요청 처리
	@ResponseBody
	public String sendChatting(@PathVariable("memberNo") int senderNo, @ModelAttribute("ChatMessage") ChatMessage msg, HttpSession session) {
		//[messageNo=0, messageContent=123, messageCount=0, messageCreate=null, messageStatus=
		log.info("msg={}", msg);
		
		
		
		
		
		
		return null;
	}
	
	
	
	
	
	
}
