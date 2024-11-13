package com.my.resumeManager.epilogue.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

public interface EpilogueService {

	int getPositiveHistoryCount(int memberNo);

	ArrayList<ResumeHistory> selectPositiveHistory(int memberNo, PageInfo pi);

	ArrayList<Epilogue> selectAllEpilogue(ArrayList<ResumeHistory> rhList);

	int insertEpilogue(Epilogue epilogue);

	int updateEpilogue(Epilogue epilogue);

	int selectEpilogueNo(int resumeNo);
	
	int deleteEpilogue(int resumeNo);

	int getEpilogueCount();

	ArrayList<Epilogue> selectAllEpiloguePage(PageInfo pi);

	ArrayList<ResumeHistory> selectAllHistory(ArrayList<Epilogue> epList);

	int getEpilogueSearchCount(HashMap<String, String> conditionMap);

	void ctxReloadCompanyName();



}
