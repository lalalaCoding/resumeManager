package com.my.resumeManager.epilogue.model.vo;

import java.sql.Date;

public class Epilogue {
	private int epilogueNo; //후기 번호
	private int epilogueWriter; //작성자
	private int resumeNo; //이력 번호
	private String epilogueContent; //후기 내용
	private Date epilogueCreate; //후기 작성일
	private char epilogueVaild; // 후기 삭제여부
}
