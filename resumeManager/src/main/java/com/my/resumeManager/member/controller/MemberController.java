package com.my.resumeManager.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
	
	@GetMapping("header.test")
	public String headerTest() {
		return "common/header";
	}
	
	@GetMapping("footer.test")
	public String footerTest() {
		return "common/footer";
	}
	
	@GetMapping("enroll.me")
	public String enrollPage() {
		return "member/enroll";
	}
	
	
	
	
}
