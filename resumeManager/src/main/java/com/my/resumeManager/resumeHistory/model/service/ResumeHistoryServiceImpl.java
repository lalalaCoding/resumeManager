package com.my.resumeManager.resumeHistory.model.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

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

	@Override
	public int getSearchCountResumeHistory(HashMap<String, String> condition) {
		return rMapper.getSearchCountResumeHistory(condition);
	}

	@Override
	public ArrayList<ResumeHistory> selectResumeHistory(HashMap<String, String> condition, PageInfo pi) {
		RowBounds rowBounds = 
					new RowBounds((pi.getCurrentPage() - 1)*pi.getBoardLimit(), pi.getBoardLimit());
		return rMapper.selectResumeHistory(condition, rowBounds);
	}

	@Override
	public ResumeHistory selectOneResumeHistory(int resumeNo) {
		return rMapper.selectOneResumeHistory(resumeNo);
	}

	@Override
	public ArrayList<ResumeCondition> selectOneResumeCondition(ResumeHistory rh) {
		return rMapper.selectOneResumeCondition(rh);
	}

	@Override
	public int updateResumeHistory(HashMap<String, Object> updMap) {
		return rMapper.updateResumeHistory(updMap);
	}

	@Override
	public int deleteResumeCondition(ArrayList<ResumeCondition> delConList) {
		return rMapper.deleteResumeCondition(delConList);
	}

	@Override
	public int deleteAllResumeHistory(HashMap<String, Object> delMap) {
		return rMapper.deleteAllResumeHistory(delMap);
	}

	@Override
	public ArrayList<CompanyType> myCompanyTypeCount(int memberNo) {
		ArrayList<CompanyType> comList = rMapper.companyTypeCount(memberNo);
		//직군 이름 : SI, SM, SOLUTION, SERVICE, START UP
		String[] companyNameArr = {"SI", "SM", "SOLUTION", "SERVICE", "START UP"};
		
		if (comList.size() < 5) {
			for (String typeName : companyNameArr) {
				boolean flag = false;
				for (CompanyType ct : comList) {
					if (ct.getTypeName().equals(typeName)) flag = true;
				}
				if (!flag) {
					CompanyType addCompanyType = new CompanyType(0, typeName, 0);
					comList.add(addCompanyType);
				}
			}
		} 
		
		return comList;
	}

	@Override
	public ArrayList<HashMap<Date, Integer>> myWeekHistoryCount(HashMap<String, Object> condition) {
		return rMapper.historyCount(condition);
	}
	
	
}
