package com.my.resumeManager.epilogue.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.epilogue.model.dao.EpilogueMapper;
import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

@Service
public class EpilogueServiceImpl implements EpilogueService{
	
	@Autowired
	private EpilogueMapper eMapper;

	@Override
	public int getPositiveHistoryCount(int memberNo) {
		return eMapper.getPositiveHistoryCount(memberNo);
	}
	
	@Override
	public ArrayList<ResumeHistory> selectPositiveHistory(int memberNo, PageInfo pi) {
		RowBounds rowBounds = new RowBounds((pi.getCurrentPage() - 1) * pi.getBoardLimit(), pi.getBoardLimit());
		return eMapper.selectPositiveHistory(memberNo, rowBounds);
	}

	@Override
	public ArrayList<Epilogue> selectAllEpilogue(ArrayList<ResumeHistory> rhList) {
		return eMapper.selectAllEpilogue(rhList);
	}

	@Override
	public int insertEpilogue(Epilogue epilogue) {
		return eMapper.insertEpilogue(epilogue);
	}

	@Override
	public int updateEpilogue(Epilogue epilogue) {
		return eMapper.updateEpilogue(epilogue);
	}

	@Override
	public int selectEpilogueNo(int resumeNo) {
		return eMapper.selectEpilogueNo(resumeNo);
	}
	
	@Override
	public int deleteEpilogue(int resumeNo) {
		return eMapper.deleteEpilogue(resumeNo);
	}

	@Override
	public int getEpilogueCount() {
		return eMapper.getEpilogueCount();
	}

	@Override
	public ArrayList<Epilogue> selectAllEpiloguePage(PageInfo pi) {
		RowBounds rowBounds = new RowBounds((pi.getCurrentPage() - 1) * pi.getBoardLimit(), pi.getBoardLimit());
		return eMapper.selectAllEpiloguePage(rowBounds);
	}

	@Override
	public ArrayList<ResumeHistory> selectAllHistory(ArrayList<Epilogue> epList) {
		return eMapper.selectAllHistory(epList);
	}

	@Override
	public int getEpilogueSearchCount(HashMap<String, String> conditionMap) {
		return eMapper.getEpilogueSearchCount(conditionMap);
	}

	@Override
	public void ctxReloadCompanyName() {
		eMapper.ctxReloadCompanyName();
	}

	@Override
	public ArrayList<Epilogue> searchEpilogue(PageInfo pi, HashMap<String, String> conditionMap) {
		RowBounds rowBounds = new RowBounds((pi.getCurrentPage() - 1)*pi.getBoardLimit(), pi.getBoardLimit());
		
		return eMapper.searchEpilogue(rowBounds, conditionMap);
	}

	


	
	
	
}
