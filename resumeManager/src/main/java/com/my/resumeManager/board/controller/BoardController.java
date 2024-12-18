package com.my.resumeManager.board.controller;


import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.resumeManager.board.model.service.BoardService;
import com.my.resumeManager.board.model.vo.BoardForm;
import com.my.resumeManager.common.page.PageInfo;
import com.my.resumeManager.common.page.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {
	
	private final BoardService bService;
	
	@GetMapping("/board/notice")
	public String noticePage(Model model, @RequestParam(value="page", defaultValue="1") int currentPage, HttpServletRequest request) {
		//페이징 처리한 게시물 정보 조회하기
		int listCount = bService.boardListCount(null);
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 5, 10);
		ArrayList<BoardForm> noticeList = bService.boardList(pi, null);
		
		model.addAttribute("noticeList", noticeList);
		model.addAttribute("loc", request.getRequestURI());
		model.addAttribute("pi", pi);
		
		return "board/noticeList";
	}
	
	
	
	
	
	
	
}
