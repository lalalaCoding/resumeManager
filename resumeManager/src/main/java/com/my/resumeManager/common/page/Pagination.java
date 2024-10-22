package com.my.resumeManager.common.page;

public class Pagination {
	public static PageInfo getPageInfo(int currentPage, int listCount, int pageLimit, int boardLimit) {
		//게시판에서 페이지 버튼의 마지막 숫자
		int maxPage = (int) Math.ceil((double)listCount / boardLimit);
		
		int groupNum = (int) Math.ceil((double)currentPage / pageLimit);
		int startPage = ((groupNum - 1)*pageLimit) + 1; //View에서 페이지 버튼의 시작 숫자
		int endPage = groupNum * pageLimit; //View에서 페이지 버튼의 마지막 숫자
		if(groupNum * pageLimit > maxPage) {
			endPage = maxPage;
		}
		
		return new PageInfo(currentPage, listCount, pageLimit, boardLimit, maxPage, startPage, endPage);
	}
}
