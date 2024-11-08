package com.my.resumeManager.epilogue.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

@Mapper
public interface EpilogueMapper {

	public Integer connectTest();

	public ArrayList<ResumeHistory> selectPositiveHistory(int memberNo);

	
	
	
	
	
	
	
}
