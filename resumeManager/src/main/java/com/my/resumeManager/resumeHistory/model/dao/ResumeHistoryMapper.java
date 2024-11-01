package com.my.resumeManager.resumeHistory.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.my.resumeManager.resumeHistory.model.vo.CompanyType;
import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;
import com.my.resumeManager.resumeHistory.model.vo.ResumeCondition;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

@Mapper
public interface ResumeHistoryMapper {

	ArrayList<PlatformType> selectAllPlatformType();

	ArrayList<ConditionInfo> selectAllConditionInfo();

	int insertResumeCondition(ArrayList<ResumeCondition> insertList);

	ArrayList<CompanyType> selectAllCompanyType();

	int insertResumeHistory(ResumeHistory resumeHistory);

	void deleteResumeHistory(int resumeNo);

	int getCountResumeHistory(int memberNo);

	ArrayList<ResumeHistory> selectAllResumeHistory(int memberNo, RowBounds rowBounds);

	ArrayList<ResumeCondition> selectAllResumeCondition(ArrayList<ResumeHistory> rhList);

	int getSearchCountResumeHistory(HashMap<String, String> condition);

	ArrayList<ResumeHistory> selectResumeHistory(HashMap<String, String> condition, RowBounds rowBounds);

	ResumeHistory selectOneResumeHistory(int resumeNo);

	ArrayList<ResumeCondition> selectOneResumeCondition(ResumeHistory rh);

}
