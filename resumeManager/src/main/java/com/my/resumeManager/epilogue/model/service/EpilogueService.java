package com.my.resumeManager.epilogue.model.service;

import java.util.ArrayList;

import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

public interface EpilogueService {

	ArrayList<ResumeHistory> selectPositiveHistory(int memberNo);

}
