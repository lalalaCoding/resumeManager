package com.my.resumeManager.resumeHistory.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.resumeHistory.model.service.ResumeHistoryService;
import com.my.resumeManager.resumeHistory.model.vo.CompanyType;
import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;
import com.my.resumeManager.resumeHistory.model.vo.ResumeCondition;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistoryException;

import jakarta.servlet.http.HttpSession;

@Controller
public class ResumeHistoryController {
	@Autowired
	private ResumeHistoryService rService;
	
	@GetMapping("resumeHistoryPage.rh")
	public String resumeHistoryPage() {
		return "resume/resumeHistory";
	}
	
	@GetMapping("insertResumeHistoryPage.rh")
	public String insertResumeHistoryPage() {
		return "resume/insertResumeHistory";
	}
	
	@GetMapping("insertResumeHistory.rh")
	public String insertResumeHistory(@ModelAttribute ResumeHistory resumeHistory,
										HttpSession session,
										@RequestParam(value="deadline", defaultValue="always") String deadline,
										@RequestParam("companyTypeName") String companyTypeName,
										@RequestParam("platformName") String platformName,
										@RequestParam("essential") String essential,
										@RequestParam("preferential") String preferential
										) {
		System.out.println(resumeHistory);
		
		//작성자 지정
		Member loginMember = (Member)session.getAttribute("loginMember");
		if(loginMember != null) {
			resumeHistory.setResumeWriter(loginMember.getMemberNo());
		}
		
		//지원마감일 가공
		if(!deadline.equals("always")) {
			StringTokenizer st = new StringTokenizer(deadline, "-");
			
			//캘린더 객체 생성 -> 캘린더에 시간을 지정 -> 밀리초 반환 -> Date 객체 생성
			Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()) - 1, Integer.parseInt(st.nextToken()));
			long date = c.getTimeInMillis();
			resumeHistory.setResumeDeadline(new Date(date)); // 데드라인 지정
		}
		
		//회사 유형 가공
		if(getCompanyType(companyTypeName) == null) {
			throw new ResumeHistoryException("서비스 요청 실패");
		} else {
			resumeHistory.setCompanyType(getCompanyType(companyTypeName));
		}
		
		//플랫폼 정보 가공
		if(getPlatformType(platformName) == null) {
			throw new ResumeHistoryException("서비스 요청 실패");
		} else {
			resumeHistory.setPlatformType(getPlatformType(platformName));
		}
		
		//지원 이력 삽입
		System.out.println("지원 이력 삽입 전 확인");
		System.out.println(resumeHistory);
		int insertHistoryResult = rService.insertResumeHistory(resumeHistory);
		//자격 조건 삽입
		String insertConditionResult = insertResumeCondition(essential, preferential, resumeHistory.getResumeNo());
		
		
		if(insertHistoryResult == 1 && insertConditionResult.equals("success")) {//삽입 성공
			System.out.println("***** 삽입 성공 *****");
			
			
		} else if(insertHistoryResult == 0) {//지원 이력 삽입 실패
			// 지원 이력 삭제 추가
			System.out.println("***** 지원 이력 삭제 로직 실행 *****");
		} else {//자격 조건 삽입 실패
			throw new ResumeHistoryException("서비스 요청 실패");
		}
		
		return null;
	}
	
	private CompanyType getCompanyType(String companyTypeName) {
		ArrayList<CompanyType> cList = rService.selectAllCompanyType();
		
		for(CompanyType c : cList) {
			if(companyTypeName.equalsIgnoreCase(c.getTypeName())) {
				return c;
			}
		}
		return null;
	}

	public PlatformType getPlatformType(String platformName) {
		ArrayList<PlatformType> pList = rService.selectAllPlatformType();
		
		String newPlatformName = "";
		
		switch(platformName) {
		case "jobKorea" : newPlatformName = "잡코리아"; break;
		case "incrute" : newPlatformName = "인크루트"; break;
		case "peopleIn" : newPlatformName = "사람인"; break;
		case "jumpIt" : newPlatformName = "점핏"; break;
		case "personal" : newPlatformName = "지인"; break;
		}
		
		for(PlatformType p : pList) {
			if(p.getPlatformName().equals(newPlatformName)) {
				return p;
			}
		}
		
		return null; // 일치하는 플랫폼 이름이 없으면 null으로 리턴
	}
	
	public String insertResumeCondition(String essential, String preferential, int resumeHistoryNo) {
		ObjectMapper mapper = new ObjectMapper();
		int result = 0;
		ArrayList<ResumeCondition> insertList = new ArrayList<ResumeCondition>();
		try {
			//RESUME_CONDITION 테이블에 삽입
			List<String> essentialList = mapper.readValue(essential, new TypeReference<List<String>>(){});
			List<String> preferentialList = mapper.readValue(preferential, new TypeReference<List<String>>(){});
		
			//인포번호 조회 -> ResumeCondition 객체 생성 -> 테이블 삽입
			ArrayList<ConditionInfo> infoList = rService.selectAllConditionInfo();
			for(ConditionInfo c : infoList) {
				for(String e : essentialList) {
					if(c.getInfoName().equalsIgnoreCase(e)) {
						insertList.add(new ResumeCondition(0, 1, c.getInfoNo(), resumeHistoryNo));
					}
				}
				for(String e : preferentialList) {
					if(c.getInfoName().equalsIgnoreCase(e)) {
						insertList.add(new ResumeCondition(0, 0, c.getInfoNo(), resumeHistoryNo));
					}
				}
			}
			
			result = rService.insertResumeCondition(insertList);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(result == insertList.size()) {
			return "success";
		} else {
			// 삭제 로직 추가해야함
			// rService.deleteResumeCondition(insertList); -> 프로시저 활용
			return "fail";
		}
	}
	
	
	
	
}
