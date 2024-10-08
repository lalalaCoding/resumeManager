package com.my.resumeManager.resumeHistory.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.resumeHistory.model.dao.ResumeHistoryMapper;

@Service
public class ResumeHistoryServiceImpl implements ResumeHistoryService {
	
	@Autowired
	private ResumeHistoryMapper rMapper;
	
	
}
