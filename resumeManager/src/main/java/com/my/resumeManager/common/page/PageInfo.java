package com.my.resumeManager.common.page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
public class PageInfo {
	private int currentPage; //View에서 현재 페이지의 수
	private int listCount; //게시판에서 전체 컨텐츠의 개수
	private int pageLimit; //View에서 페이지 버튼의 개수
	private int boardLimit; //View에서 출력하려는 컨텐츠의 개수
	private int maxPage; //게시판에서 페이지 버튼의 마지막 숫자
	private int startPage; //View에서 페이지 버튼의 시작 숫자
	private int endPage; //View에서 페이지 버튼의 마지막 숫자
}
