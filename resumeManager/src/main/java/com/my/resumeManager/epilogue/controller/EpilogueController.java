package com.my.resumeManager.epilogue.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.my.resumeManager.epilogue.model.service.EpilogueService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EpilogueController {
	
	@Autowired
	private EpilogueService eService;
	
	@GetMapping("resumeEpliloguePage.re")
	public String resumeEpliloguePage() {
		
		return "epilogue/epilogue";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
