package com.my.resumeManager.epilogue.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.epilogue.model.dao.EpilogueMapper;
import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

@Service
public class EpilogueServiceImpl implements EpilogueService{
	
	@Autowired
	private EpilogueMapper eMapper;

	@Override
	public ArrayList<ResumeHistory> selectPositiveHistory(int memberNo) {
		return eMapper.selectPositiveHistory(memberNo);
	}

	
	
	
}
