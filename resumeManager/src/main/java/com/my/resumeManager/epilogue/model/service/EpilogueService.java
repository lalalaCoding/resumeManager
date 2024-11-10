package com.my.resumeManager.epilogue.model.service;

import java.util.ArrayList;

import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

public interface EpilogueService {

	int getPositiveHistoryCount(int memberNo);

	ArrayList<ResumeHistory> selectPositiveHistory(int memberNo, PageInfo pi);

	ArrayList<Epilogue> selectAllEpilogue(ArrayList<ResumeHistory> rhList);

	int insertEpilogue(Epilogue epilogue);


}
