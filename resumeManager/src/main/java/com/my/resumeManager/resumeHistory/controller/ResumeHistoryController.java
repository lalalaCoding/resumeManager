package com.my.resumeManager.resumeHistory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.my.resumeManager.resumeHistory.model.service.ResumeHistoryService;

@Controller
public class ResumeHistoryController {
	@Autowired
	private ResumeHistoryService rService;
	
	@GetMapping("resumeHistoryPage.rh")
	public String resumeHistoryPage() {
		return "resume/resumeHistory";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
