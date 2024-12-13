package com.my.resumeManager.resumeHistory.model.dao;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
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

	int updateResumeHistory(@Param("updMap") HashMap<String, Object> updMap);

	int deleteResumeCondition(ArrayList<ResumeCondition> delConList);

	int deleteAllResumeHistory(@Param("delMap") HashMap<String, Object> delMap);

	ArrayList<CompanyType> companyTypeCount(int memberNo);
	
	@MapKey("resume_date")
	ArrayList<HashMap<Date, Integer>> historyCount(@Param("condition") HashMap<String, Object> condition);

}
