package com.my.resumeManager.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class HourSalaryTest {
	
	@Test
	public void hourSalaryTest(@Value("${hourSalary.API_ENCODING_KEY}") String serviceKey) {
		try {
			URL url = new URL("https://api.odcloud.kr/api/15068774/v1/uddi:ea28d355-6222-40db-8237-ceda86c5675d?page=1&perPage=10&serviceKey="+serviceKey); // 호출할 외부 API 를 입력한다.

			HttpURLConnection conn = (HttpURLConnection) url.openConnection(); //header에 데이터 통신 방법을 지정한다.
			conn.setRequestMethod("GET"); //GET 방식으로 통신요청
			conn.setRequestProperty("Content-Type", "application/json; utf-8");

			// 응답
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			String response = in.readLine();
			
			//자바 객체로 가공해보자
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> responseMap = mapper.readValue(response, new TypeReference<Map<String, Object>>(){});
			
			ArrayList<Map<String, Integer>> responseData = (ArrayList)responseMap.get("data");
			Map<String, Integer> recentData = responseData.get(0); //{순번=1, 시간급=9860, 연도=2024}
			int hourSalary = recentData.get("시간급"); //9860
			
			in.close();
			conn.disconnect();
			
			assertThat(hourSalary).isEqualTo(9860);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
}
