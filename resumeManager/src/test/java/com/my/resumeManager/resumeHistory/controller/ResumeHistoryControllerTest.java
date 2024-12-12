package com.my.resumeManager.resumeHistory.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.resumeManager.resumeHistory.model.service.ResumeHistoryService;
import com.my.resumeManager.resumeHistory.model.vo.CompanyType;

@SpringBootTest
public class ResumeHistoryControllerTest {
	
	@Autowired ResumeHistoryService rService;
	
	@Test
	public void 전체_지원직군_조회() {
		//given
		int memberNo = 1;
		
		//when
		ArrayList<CompanyType> comList = rService.myCompanyTypeCount(memberNo);
		
		//then
		Assertions.assertThat(comList.size()).isEqualTo(5);
		Assertions.assertThat(comList).extracting(t -> t.getTypeCount() >= 0);
	}
	
	@Test
	public void 일주일_지원건수_조회() {
		//given
		int memberNo = 1;
		Date today = new Date(2024-1900, 9, 26); // 2024-10-26
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("memberNo", memberNo);
		condition.put("date", today);
		
		//when
		ArrayList<HashMap<Date, Integer>> weekList = rService.myWeekHistoryCount(condition);
		
		//then
		Assertions.assertThat(weekList).isNotEmpty();
	}
	
	
	
	
	
	
}
