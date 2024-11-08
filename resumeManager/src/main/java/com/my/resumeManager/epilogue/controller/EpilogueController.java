package com.my.resumeManager.epilogue.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.my.resumeManager.epilogue.model.service.EpilogueService;
import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.epilogue.model.vo.EpilogueException;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EpilogueController {
	
	@Autowired
	private EpilogueService eService;
	
	private static int memberNo; //현재 로그인된 사용자의 회원번호 저장
	
	
	@GetMapping("epiloguePage.ep")
	public String epiloguePage(HttpSession session, Model model) {
		model.addAttribute("info", "epilogue"); //사이드 메뉴바의 효과 조정을 위한 정보
		
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember != null) {
			memberNo = loginMember.getMemberNo();
		} else {
			throw new EpilogueException("로그인 후 이용해주세요.");
		}
		
		//RESUME_STATE > 0, RESUME_WRITER = memberNo, RESUME_VAILD = 'Y'인 데이터 조회하기
		ArrayList<ResumeHistory> rhList = eService.selectPositiveHistory(memberNo);
		log.info("rhList={}", rhList);
		model.addAttribute("rhList", rhList);
		
		//유효한 지원 이력에 대한 후기 정보 조회
		//ArrayList<Epilogue> epList = eService.selectAllEpilogue(rhList);
		
		
		
		
		
		return "member/epilogueInfo";
	}
	
	
	
	
	
	
	
	@GetMapping("resumeEpliloguePage.re")
	public String resumeEpliloguePage() {
		
		return "epilogue/epilogue";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
