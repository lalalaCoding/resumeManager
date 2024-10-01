package com.my.resumeManager.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.resumeManager.member.model.service.MemberService;

@Controller
public class MemberController {
	@Autowired
	private MemberService mService;
	
	
	@GetMapping("header.test")
	public String headerTest() {
		return "common/header";
	}
	
	@GetMapping("footer.test")
	public String footerTest() {
		return "common/footer";
	}
	
	@GetMapping("enroll.me") // 회원가입 페이지로 이동
	public String enrollPage() {
		return "member/enroll";
	}
	
	@GetMapping("checkId.me")
	@ResponseBody
	public String checkId(@RequestParam("memberId") String memberId) {
		int result = mService.checkId(memberId);
		return result == 0 ? "yes" : "no";
	}
	
	
	
	
	
	
	
	
	
}
