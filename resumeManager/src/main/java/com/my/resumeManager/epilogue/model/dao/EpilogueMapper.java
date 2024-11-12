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

	public int insertEpilogue(Epilogue epilogue);

	public int updateEpilogue(Epilogue epilogue);

	public int selectEpilogueNo(int resumeNo);
	
	public int deleteEpilogue(int resumeNo);

	public int getEpilogueCount();

	public ArrayList<Epilogue> selectAllEpiloguePage(RowBounds rowBounds);

	public ArrayList<ResumeHistory> selectAllHistory(@Param("epList") ArrayList<Epilogue> epList);




	
	
	
	
	
	
	
}
