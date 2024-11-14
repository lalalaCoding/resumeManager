package com.my.resumeManager.epilogue.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.common.page.Pagination;
import com.my.resumeManager.epilogue.model.service.EpilogueService;
import com.my.resumeManager.epilogue.model.vo.Epilogue;
import com.my.resumeManager.epilogue.model.vo.EpilogueException;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.resumeHistory.model.vo.ResumeHistory;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class EpilogueController {
	
	@Autowired
	private EpilogueService eService;
	
	private static int memberNo; //현재 로그인된 사용자의 회원번호 저장
	
	
	@GetMapping("epiloguePage.ep") //나의 후기 관리 페이지로 이동
	public String epiloguePage(HttpSession session, Model model,
								@RequestParam(value="page", defaultValue="1") int currentPage,
								HttpServletRequest request) {
		model.addAttribute("info", "epilogue"); //사이드 메뉴바의 효과 조정을 위한 정보
		model.addAttribute("loc", request.getRequestURI());
		
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember != null) {
			memberNo = loginMember.getMemberNo();
		} else {
			throw new EpilogueException("로그인 후 이용해주세요.");
		}
		
		//페이지 정보
		int listCount = eService.getPositiveHistoryCount(memberNo);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5, 9);
		model.addAttribute("pi", pi);
		
		//RESUME_STATE > 0, RESUME_WRITER = memberNo, RESUME_VAILD = 'Y'인 데이터 조회하기
		ArrayList<ResumeHistory> rhList = eService.selectPositiveHistory(memberNo, pi);
		log.info("rhList={}", rhList);
		model.addAttribute("rhList", rhList);
		
		//유효한 지원 이력에 대한 후기 정보 조회
		ArrayList<Epilogue> epList = null;
		if (!rhList.isEmpty()) {
			epList = eService.selectAllEpilogue(rhList);
		}
		log.info("epList={}", epList);
		model.addAttribute("epList", epList);
		
		//지원 이력에 대한 후기의 존재 여부를 판단할 수 있는 데이터 만들기
		TreeMap<Integer, Integer> tree = new TreeMap<>();
		for (ResumeHistory rh : rhList) {
			for (Epilogue ep : epList) {
				if (rh.getResumeNo() == ep.getResumeNo()) {
					tree.put(rh.getResumeNo(), ep.getEpilogueNo());
					break;
				}
			}
			
			if (!tree.containsKey(rh.getResumeNo())) {
				tree.put(rh.getResumeNo(), 0);
			}
		}
		model.addAttribute("tree", tree);
		
		
		return "member/epilogueInfo";
	}
	
	
	@PostMapping("insertEpilogue.ep")
	@ResponseBody
	public String insertEpilogue(@ModelAttribute Epilogue epilogue, HttpSession session) {
		log.info("후기 작성요청={}", epilogue);
		//post요청이기 때문에 본인이 작성한 이력에 대한 후기 작성요청인지는 검사할 필요가 없다.
		//바로 등록 ㄱㄱ
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember != null) {
			memberNo = loginMember.getMemberNo();
			epilogue.setEpilogueWriter(memberNo);
		} else {
			throw new EpilogueException("로그인 후 이용해주세요.");
		}
		
		int insertResult = eService.insertEpilogue(epilogue);
		
		return insertResult > 0 ? "success" : "fail";
	}
	
	@PostMapping("updateEpilogue.ep")
	@ResponseBody
	public String updateEpilogue(@ModelAttribute Epilogue epilogue) {
		log.info("후기 수정요청={}", epilogue);
		int updateResult = eService.updateEpilogue(epilogue);
		
		return updateResult > 0 ? "success" : "fail";
	}
	
	@PostMapping("deleteEpilogue.ep")
	@ResponseBody
	public String deleteEpilogue(@RequestParam("resumeNo") int resumeNo) {
		log.info("후기 삭제(지원 이력 번호)={}", resumeNo);
		int epilogueNo = eService.selectEpilogueNo(resumeNo);
		int deleteResult = eService.deleteEpilogue(epilogueNo);
		
		return deleteResult > 0 ? "success" : "fail";
	}
	
	@GetMapping("resumeEpliloguePage.ep") //일반 후기 목록 출력
	public String resumeEpliloguePage(@RequestParam(value="page", defaultValue="1") int currentPage, Model model,
										HttpServletRequest request,
										@RequestParam(value="companyName", required = false) String companyName) {
		
		log.info("검색조건={}",companyName);
		HashMap<String, String> conditionMap = new HashMap<>(); //검색 조건을 Map에 저장 : 검색 조건이 추가될 확장성을 고려
		if(companyName != null) {
			conditionMap.put("companyName", companyName);
			eService.ctxReloadCompanyName();
		}
		int listCount = eService.getEpilogueCount(conditionMap);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5, 9);
		ArrayList<Epilogue> epList = eService.selectAllEpiloguePage(pi, conditionMap);
		ArrayList<ResumeHistory> rhList = null;
		if (!epList.isEmpty()) {
			rhList = eService.selectAllHistory(epList);
		}
		
		log.info("listCount={}",listCount);
		log.info("후기 목록={}", epList);
		log.info("후기 목록={}", epList);
		
		model.addAttribute("conditionMap", conditionMap);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("epList", epList);
		model.addAttribute("rhList", rhList);
		model.addAttribute("pi", pi);
		
		return "epilogue/epilogue";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
