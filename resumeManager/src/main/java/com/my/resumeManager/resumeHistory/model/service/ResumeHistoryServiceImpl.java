package com.my.resumeManager.resumeHistory.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.resumeHistory.model.dao.ResumeHistoryMapper;
import com.my.resumeManager.resumeHistory.model.vo.CompanyType;
import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;
import com.my.resumeManager.resumeHistory.model.vo.ResumeCondition;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

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

	@Override
	public int insertResumeCondition(ArrayList<ResumeCondition> insertList) {
		return rMapper.insertResumeCondition(insertList);
	}

	@Override
	public ArrayList<CompanyType> selectAllCompanyType() {
		return rMapper.selectAllCompanyType();
	}

	@Override
	public int insertResumeHistory(ResumeHistory resumeHistory) {
		return rMapper.insertResumeHistory(resumeHistory);
	}
	
	
}
