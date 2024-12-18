package com.my.resumeManager.board.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import com.my.resumeManager.board.model.dao.BoardMapper;
import com.my.resumeManager.board.model.vo.BoardForm;
import com.my.resumeManager.common.page.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardMapper bMapper;

	public int boardListCount(Character boardType) {
		return bMapper.selectBoardCount(boardType);
	}

	public ArrayList<BoardForm> boardList(PageInfo pi, Character boardType) {
		RowBounds rowBounds = new RowBounds((pi.getCurrentPage() - 1) * pi.getBoardLimit(), pi.getBoardLimit());
		
		return bMapper.selectBoardList(rowBounds, boardType);
	}
	
	
}
