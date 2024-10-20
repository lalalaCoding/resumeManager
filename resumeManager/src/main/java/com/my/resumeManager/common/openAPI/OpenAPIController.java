package com.my.resumeManager.common.openAPI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OpenAPIController {
	
	private final String HOUR_SALARY_KEY;
	
	public OpenAPIController(@Value("${hourSalary.API_ENCODING_KEY}") String serviceKey) {
		this.HOUR_SALARY_KEY = serviceKey;
	}
	
	//최저 시급
	public int getHourSalary() {
		int hourSalary = 0;
		
		try {
			URL url = new URL("https://api.odcloud.kr/api/15068774/v1/uddi:ea28d355-6222-40db-8237-ceda86c5675d?page=1&perPage=10&serviceKey="+HOUR_SALARY_KEY); // 호출할 외부 API 를 입력한다.

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //header에 데이터 통신 방법을 지정한다.
			conn.setRequestMethod("GET"); //GET 방식으로 통신요청
			conn.setRequestProperty("Content-Type", "application/json; utf-8");

			// 응답
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String response = in.readLine();
			
			//자바 객체로 가공
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> responseMap = mapper.readValue(response, new TypeReference<Map<String, Object>>(){});
			
			ArrayList<Map<String, Integer>> responseData = (ArrayList)responseMap.get("data");
			Map<String, Integer> recentData = responseData.get(0); //{순번=1, 시간급=9860, 연도=2024}
			hourSalary = recentData.get("시간급"); //9860
			
			in.close();
			conn.disconnect();

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return hourSalary;
	}
	
	
	
	
	
}
