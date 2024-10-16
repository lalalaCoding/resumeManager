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
public class ResumeCondition extends ConditionInfo{
	private int conditionNo;
	private int resumeNo;
	private int conditionType; // 0(우대), 1(필수)
	private int infoNo;
}
