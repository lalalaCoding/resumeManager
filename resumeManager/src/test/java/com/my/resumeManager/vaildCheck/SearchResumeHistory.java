package com.my.resumeManager.vaildCheck;

import java.util.Calendar;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class SearchResumeHistory {
	
	@Test
	public void searchResumeHistory() {
		String companyName = "";
		String beginDt = "2024-10-01";
		String endDt = "2024-10-15";
		String infoName = "";
		
		boolean vaildFlag = false;
		if (companyName.trim().length() > 0) { //회사명 데이터가 존재
			vaildFlag = true;
		} 
		if (beginDt.trim().length() > 0 && endDt.trim().length() > 0) { // 시작날짜 종료날짜 모두 존재
			String[] beginArr = beginDt.split("-");
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.set(Integer.parseInt(beginArr[0]), Integer.parseInt(beginArr[1]), Integer.parseInt(beginArr[2]));
			
			String[] endArr = endDt.split("-");
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.set(Integer.parseInt(endArr[0]), Integer.parseInt(endArr[1]), Integer.parseInt(endArr[2]));
			
			vaildFlag = beginCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis() ? true : false;
		}
		if (infoName.trim().length() > 0) {
			vaildFlag = true;
		}
		
		assertThat(vaildFlag).isTrue();
	}
	
	
	
}
