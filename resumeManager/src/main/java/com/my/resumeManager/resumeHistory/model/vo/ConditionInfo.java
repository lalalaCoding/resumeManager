package com.my.resumeManager.resumeHistory.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ConditionInfo { // 지원 자격 조건에 대한 서브 테이블의 VO
	private int infoNo;
	private int infoType; // 101(언어) 102(라이브러리) 103(프레임워크) 104(데이터베이스) 201(CS) 202(경험) 203(툴)
	private String infoName;
}
