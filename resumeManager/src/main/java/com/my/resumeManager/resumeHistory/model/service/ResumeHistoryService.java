package com.my.resumeManager.resumeHistory.model.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

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

	int getSearchCountResumeHistory(HashMap<String, String> condition);

	ArrayList<ResumeHistory> selectResumeHistory(HashMap<String, String> condition, PageInfo pi);

	ResumeHistory selectOneResumeHistory(int resumeNo);

	ArrayList<ResumeCondition> selectOneResumeCondition(ResumeHistory rh);

	int updateResumeHistory(HashMap<String, Object> updMap);

	int deleteResumeCondition(ArrayList<ResumeCondition> delConList);
	
	int deleteAllResumeHistory(HashMap<String, Object> delMap);

	ArrayList<CompanyType> myCompanyTypeCount(int memberNo);

	ArrayList<HashMap<Date, Integer>> myWeekHistoryCount(HashMap<String, Object> condition);

	ArrayList<HashMap<Date, Integer>> accumulateHistoryCount(HashMap<String, Object> condition);


}
