package com.my.resumeManager.resumeHistory.model.service;

import java.util.ArrayList;

import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.resumeHistory.model.vo.CompanyType;
import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;
import com.my.resumeManager.resumeHistory.model.vo.ResumeCondition;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

public interface ResumeHistoryService {

	ArrayList<PlatformType> selectAllPlatformType();

	ArrayList<ConditionInfo> selectAllConditionInfo();

	int insertResumeCondition(ArrayList<ResumeCondition> insertList);

	ArrayList<CompanyType> selectAllCompanyType();

	int insertResumeHistory(ResumeHistory resumeHistory);

	void deleteResumeHistory(int resumeNo);

	int getCountResumeHistory(int memberNo);

	ArrayList<ResumeHistory> selectAllResumeHistory(int memberNo, PageInfo pi);

	ArrayList<ResumeCondition> selectAllResumeCondition(ArrayList<ResumeHistory> rhList);

}
