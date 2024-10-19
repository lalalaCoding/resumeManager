package com.my.resumeManager.resumeHistory.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

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

}
