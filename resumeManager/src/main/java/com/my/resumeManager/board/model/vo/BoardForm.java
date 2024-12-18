package com.my.resumeManager.board.model.vo;

import java.sql.Date;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardForm {
	
	private int boardNo;
	private String boardTitle;
	private String boardContent;
	@NotEmpty(message = "작성자가 존재하지 않습니다.")
	private int boardWriter;
	private int boardCount;
	private Date boardCreate;
	private Date boardModify;
	private char boardStatus;
	private char boardType;
}
