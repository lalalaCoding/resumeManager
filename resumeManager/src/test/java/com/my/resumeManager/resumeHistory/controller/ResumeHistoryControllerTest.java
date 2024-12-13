package com.my.resumeManager.resumeHistory.controller;

import java.sql.Date;
import java.time.LocalDateTime;
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
		
		Calendar cal = Calendar.getInstance();
		Date endDay = new Date(cal.getTimeInMillis());
		cal.add(Calendar.DATE, -6);
		Date beginDay = new Date(cal.getTimeInMillis());
		
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("memberNo", memberNo);
		condition.put("endDay", endDay);
		condition.put("beginDay", beginDay);
		
		//when
		ArrayList<HashMap<Date, Integer>> weekList = rService.myWeekHistoryCount(condition);
		
		//then
		Assertions.assertThat(weekList).isNotEmpty();
	}
	
	@Test
	public void 이번달_지원누적건수_조회() {
		//given
		int memberNo = 1;
		
		Calendar cal = Calendar.getInstance();
		Date endDay = new Date(cal.getTimeInMillis()); //오늘 날짜
		
		Calendar cal2 = Calendar.getInstance();
		int year = cal2.get(Calendar.YEAR);
		int month = cal2.get(Calendar.MONTH);
		
		cal2.set(year, month, 1);
		Date beginDay = new Date(cal2.getTimeInMillis()); //이번달 첫 날짜
		
		System.out.println(beginDay);
		
		//when
		
		
		
		//then
		Assertions.assertThat(beginDay.toString()).isEqualTo("2024-12-01");
		
		
		
	}
	
	
	
	
}
