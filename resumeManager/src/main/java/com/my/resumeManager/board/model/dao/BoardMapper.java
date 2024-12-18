package com.my.resumeManager.board.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import com.my.resumeManager.board.model.vo.BoardForm;

@Mapper
public interface BoardMapper {

	int selectBoardCount(Character boardType);

	ArrayList<BoardForm> selectBoardList(RowBounds rowBounds, Character boardType);

}
