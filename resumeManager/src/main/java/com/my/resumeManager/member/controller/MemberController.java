package com.my.resumeManager.member.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.my.resumeManager.common.gcs.GCSController;
import com.my.resumeManager.common.gcs.GCSRequest;
import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;

@Controller
public class MemberController {
	@Autowired
	private MemberService mService;
	
	@Autowired
	private GCSController gContoller;
	
	
	
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
	
	@PostMapping("test.me")
	public String test(@ModelAttribute Member m, @ModelAttribute GCSRequest profile) {
		profile.setName("테스트용 사진"); // 저장될 파일의 이름으로 지정이 되지 않는다.
		MultipartFile file = profile.getFile();
		String originalFilename = file.getOriginalFilename();
		
		
		
		
		try {
			gContoller.objectUpload(profile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		return "index";
	}
	
	
	
	
	
	
}
