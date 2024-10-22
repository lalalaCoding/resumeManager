package com.my.resumeManager.resumeHistory.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.common.page.PageInfo;
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

	@Override
	public void deleteResumeHistory(int resumeNo) {
		rMapper.deleteResumeHistory(resumeNo);
	}

	@Override
	public int getCountResumeHistory(int memberNo) {
		return rMapper.getCountResumeHistory(memberNo);
	}

	@Override
	public ArrayList<ResumeHistory> selectAllResumeHistory(int memberNo, PageInfo pi) {
		RowBounds rowBounds =
				new RowBounds((pi.getCurrentPage() - 1)*pi.getBoardLimit(), pi.getBoardLimit());
		return rMapper.selectAllResumeHistory(memberNo, rowBounds);
	}

	@Override
	public ArrayList<ResumeCondition> selectAllResumeCondition(ArrayList<ResumeHistory> rhList) {
		return rMapper.selectAllResumeCondition(rhList);
	}
	
	
}
