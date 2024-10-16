package com.my.resumeManager.resumeHistory.model.service;

import java.util.ArrayList;

import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;

public interface ResumeHistoryService {

	ArrayList<PlatformType> selectAllPlatformType();

	ArrayList<ConditionInfo> selectAllConditionInfo();

}
