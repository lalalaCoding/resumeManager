package com.my.resumeManager.resumeHistory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.resumeManager.resumeHistory.model.service.ResumeHistoryService;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

@Controller
public class ResumeHistoryController {
	@Autowired
	private ResumeHistoryService rService;
	
	@GetMapping("resumeHistoryPage.rh")
	public String resumeHistoryPage() {
		return "resume/resumeHistory";
	}
	
	@GetMapping("insertResumeHistoryPage.rh")
	public String insertResumeHistoryPage() {
		return "resume/insertResumeHistory";
	}
	
	@GetMapping("insertResumeHistory.rh")
	public String insertResumeHistory(@ModelAttribute ResumeHistory resumeHistory, 
										@RequestParam("platformName") String platformName
										) {
		System.out.println(resumeHistory);
		
		//회사명에 따라 시-구 정도를 자동 조회
		//
		
		return null;
	}
	
	
	
	
	
	
	
	
}
