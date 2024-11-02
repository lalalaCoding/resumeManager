package com.my.resumeManager.resumeHistory.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.resumeManager.common.openAPI.OpenAPIController;
import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.common.page.Pagination;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.resumeHistory.model.service.ResumeHistoryService;
import com.my.resumeManager.resumeHistory.model.vo.CompanyType;
import com.my.resumeManager.resumeHistory.model.vo.ConditionInfo;
import com.my.resumeManager.resumeHistory.model.vo.PlatformType;
import com.my.resumeManager.resumeHistory.model.vo.ResumeCondition;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistoryException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ResumeHistoryController {
	//@Autowired
	private ResumeHistoryService rService;
	
	//@Autowired
	private OpenAPIController oController;
	private static int yearSalary;
	
	//컴포넌트 스캔 -> 빈 등록 시점에서 의존성을 주입하여 컨트롤러 빈을 등록해준다.
	public ResumeHistoryController(ResumeHistoryService rService, OpenAPIController oController) {
		this.rService = rService;
		this.oController = oController;
		int hourSalary = oController.getHourSalary();
		double monthAvgWeek = (double)365/12/7; //월별 평균 주차 -> 4.35주
		int weekWorkTime = 8*5 + 8; //주별 평균 근로 시간
		int monthWorkTime =  (int)Math.ceil(weekWorkTime * monthAvgWeek); //월별 평균 근로 시간, 약 209시간
		int monthSalary = monthWorkTime * hourSalary; //월별 최저 임금
		yearSalary = monthSalary * 12; //최저 연봉 24,728,880		
	}
	
	@GetMapping("resumeHistoryPage.rh")
	public String resumeHistoryPage(HttpSession session, 
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			Model model, HttpServletRequest request) {
		
		Member loginMember = (Member)session.getAttribute("loginMember");
		log.info("로그인 체크 = {}", loginMember);
		
		int memberNo = 0;
		if(loginMember != null) {
			memberNo = loginMember.getMemberNo();
			//페이지 계산
			int listCount = rService.getCountResumeHistory(memberNo);
			PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5, 10);
			
			//페이지 처리된 '지원 이력' 조회
			ArrayList<ResumeHistory> rhList = rService.selectAllResumeHistory(memberNo, pi);
			
			//지원 이력 -> '지원 조건' 조회 : 페이징 처리 된 지원 이력 번호는 최대 10개이므로 IN 연산자로 SQL을 작성함			
			ArrayList<ResumeCondition> conList = rService.selectAllResumeCondition(rhList); 
			//데이터 전달
			model.addAttribute("pi", pi);
			model.addAttribute("loc", request.getRequestURI());
			model.addAttribute("rhList", rhList);
			model.addAttribute("conList", conList);
		}
		
		return "resume/resumeHistory";
	}
	
	@GetMapping("insertResumeHistoryPage.rh")
	public String insertResumeHistoryPage(Model model) {
		// 최저 시급 계산하여 전달
		model.addAttribute("yearSalary", yearSalary);
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
		//작성자 지정
		Member loginMember = (Member)session.getAttribute("loginMember");
		System.out.println("로그인 회원 확인");
		System.out.println(loginMember);
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
		int insertHistoryResult = rService.insertResumeHistory(resumeHistory);
		
		if(insertHistoryResult > 0) { //지원 이력 삽입 성공
			//자격 조건 삽입
			String insertConditionResult = insertResumeCondition(essential, preferential, resumeHistory.getResumeNo());
			if(insertConditionResult.equals("success")) { //자격 조건 삽입 성공
				//지원 이력 조회 페이지로 이동
			} else { //자격 조건 삽입 실패 : 자격 조건 삭제 -> 지원 이력 삭제
				rService.deleteResumeHistory(resumeHistory.getResumeNo());
				throw new ResumeHistoryException("서비스 요청 실패");
			}
		} else { //지원 이력 삽입 실패
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
//ResumeCondition(int conditionNo, int resumeNo, int conditionType, int infoNo, int infoType, String infoName)
						insertList.add(new ResumeCondition(0, resumeHistoryNo, 1, c.getInfoNo(), c.getInfoType(), c.getInfoName()));
					}
				}
				for(String e : preferentialList) {
					if(c.getInfoName().equalsIgnoreCase(e)) {
						insertList.add(new ResumeCondition(0, resumeHistoryNo, 0, c.getInfoNo(), c.getInfoType(), c.getInfoName()));
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
			return "fail";
		}
	}
	
	@GetMapping("searchResumeHistory.rh")
	public String searchResumeHistory(@RequestParam("companyName") String companyName,
										@RequestParam("beginDt") String beginDt,
										@RequestParam("endDt") String endDt,
										@RequestParam("infoName") String infoName,
										@RequestParam(value="page", defaultValue="1") int currentPage,
										HttpSession session, Model model, HttpServletRequest request) {
		Member loginMember = (Member)session.getAttribute("loginMember");
		String memberNo = "";
		if (loginMember != null) {
			memberNo = String.valueOf(loginMember.getMemberNo());
		} 
		
		//데이터 체크 : 입력값이 없으면 null이 아니라 ""로 넘어옴
		//유효성 검사 (url을 통해 접근할 수 있기 때문)
		boolean vaildFlag = false;
		if (companyName.trim().length() > 0) { //회사명 데이터가 존재
			vaildFlag = true;
		} else {
			companyName = null;
		}
		if (beginDt.trim().length() > 0 && endDt.trim().length() > 0) { // 시작날짜 종료날짜 모두 존재
			String[] beginArr = beginDt.split("-");
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.set(Integer.parseInt(beginArr[0]), Integer.parseInt(beginArr[1]), Integer.parseInt(beginArr[2]));
			
			String[] endArr = endDt.split("-");
			Calendar endCalendar = Calendar.getInstance();
			endCalendar.set(Integer.parseInt(endArr[0]), Integer.parseInt(endArr[1]), Integer.parseInt(endArr[2]));
			
			vaildFlag = beginCalendar.getTimeInMillis() <= endCalendar.getTimeInMillis() ? true : false;
		} else {
			beginDt = null;
			endDt = null;
		}
		if (infoName.trim().length() > 0) {
			vaildFlag = true;
		} else {
			infoName = null;
		}
		
		if (vaildFlag) { //유효성 통과
			HashMap<String, String> condition = new HashMap<String, String>(); 
			condition.put("memberNo", memberNo);
			condition.put("companyName", companyName);
			condition.put("beginDt", beginDt);
			condition.put("endDt", endDt);
			condition.put("infoName", infoName);
			
			int listCount = rService.getSearchCountResumeHistory(condition);
			PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5, 10);
			
			//페이지 처리된 '지원 이력' 조회
			ArrayList<ResumeHistory> rhList = rService.selectResumeHistory(condition, pi);
			
			//지원 이력 -> '지원 조건' 조회 : 페이징 처리 된 지원 이력 번호는 최대 10개이므로 IN 연산자로 SQL을 작성함			
			ArrayList<ResumeCondition> conList = rService.selectAllResumeCondition(rhList); 
			
			model.addAttribute("pi", pi);
			model.addAttribute("loc", request.getRequestURI());
			model.addAttribute("rhList", rhList);
			model.addAttribute("conList", conList);
			
			
			return "resume/resumeHistory";
		} else {
			throw new ResumeHistoryException("서비스 요청에 실패하였습니다.");
		}
	}
	
	
	@GetMapping("updateResumeHistoryPage.rh")
	public String updateResumeHistoryPage(@RequestParam("resumeNo") int resumeNo, 
										@RequestParam("page") int currentPage,
										HttpSession session, Model model) {
		//세션에 있는 회원 번호 추출
		Member loginMember = (Member)session.getAttribute("loginMember");
		int memberNo = 0;
		if (loginMember != null) {
			memberNo = loginMember.getMemberNo();
		} else {
			throw new ResumeHistoryException("로그인 후 이용해주세요.");
		}
		
		//지원 이력 번호에 대한 작성자 vs 회원 번호를 비교한다.
		//	일치 -> 정상적으로 수정 로직 시행
		//	불일치 -> 에러 발생시켜야함
		ResumeHistory rh = rService.selectOneResumeHistory(resumeNo);
		
		if (rh == null) { //수정하려는 이력 번호 데이터가 존재하지 않은 경우 : url을 통한 비정상적인 접근을 차단
			throw new ResumeHistoryException("비정상적인 접근입니다.");
		} else { //수정하려는 이력 번호가 데이터가 존재하는 경우
			if (rh.getResumeWriter() == memberNo) { //수정하려는 지원 이력의 작성자와 현재 로그인 회원이 일치함
				ArrayList<ResumeCondition> rcList = rService.selectOneResumeCondition(rh);
				
				StringBuffer essentialBuffer = new StringBuffer("");
				StringBuffer preferentialBuffer = new StringBuffer("");
				for (ResumeCondition con : rcList) {
					if (con.getConditionType() == 1) { //필수
						if (con.getInfoNo() < 10) {
							essentialBuffer.append("0" + con.getInfoNo() + "_"); // 1 -> 01
						} else {
							essentialBuffer.append(con.getInfoNo() + "_");
						}
					} else { //우대
						if (con.getInfoNo() < 10) {
							preferentialBuffer.append("0" + con.getInfoNo() + "_");
						} else {
							preferentialBuffer.append(con.getInfoNo() + "_");
						}
					}
				}
				
				model.addAttribute("rh", rh);
				model.addAttribute("essentialInfoNo", essentialBuffer.toString());
				model.addAttribute("preferentialInfoNo", preferentialBuffer.toString());
				model.addAttribute("currentPage", currentPage);
				model.addAttribute("yearSalary", yearSalary);
				
				return "resume/updateResumeHistory";
			} else { //수정하려는 지원 이력의 작성자와 현재 로그인 회원이 일치하지 않음
				throw new ResumeHistoryException("비정상적인 접근입니다.");
			}
		}
	}
	
	@GetMapping("updateResumeHistory.rh")
	public String updateResumeHistory(@ModelAttribute ResumeHistory resumeHistory,
										HttpSession session,
										@RequestParam(value="deadline", defaultValue="always") String deadline,
										@RequestParam("companyTypeName") String companyTypeName,
										@RequestParam("platformName") String platformName,
										@RequestParam("essential") String essential,
										@RequestParam("preferential") String preferential) {
		
		
		
		
		return null;
	}
	
	
	
}
