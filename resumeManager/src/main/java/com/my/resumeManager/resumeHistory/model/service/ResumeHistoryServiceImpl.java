package com.my.resumeManager.resumeHistory.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.resumeHistory.model.dao.ResumeHistoryMapper;
import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;

@Service
public class ResumeHistoryServiceImpl implements ResumeHistoryService {
	
	@Autowired
	private ResumeHistoryMapper rMapper;

	@Override
	public ArrayList<PlatformType> selectAllPlatformType() {
		return rMapper.selectAllPlatformType();
	}

	@Override
	public ArrayList<ConditionInfo> selectAllConditionInfo() {
		return rMapper.selectAllConditionInfo();
	}
	
	
}
