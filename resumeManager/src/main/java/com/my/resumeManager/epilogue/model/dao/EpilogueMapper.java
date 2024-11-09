package com.my.resumeManager.epilogue.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

@Mapper
public interface EpilogueMapper {

	public Integer connectTest();

	public int getPositiveHistoryCount(int memberNo);

	public ArrayList<ResumeHistory> selectPositiveHistory(int memberNo, RowBounds rowBounds);
	
	public ArrayList<Epilogue> selectAllEpilogue(@Param("rhList") ArrayList<ResumeHistory> rhList);



	
	
	
	
	
	
	
}
