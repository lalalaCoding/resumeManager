package com.my.resumeManager.resumeHistory.model.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class ResumeCondition extends ConditionInfo{
	private int conditionNo;
	private int resumeNo;
	private int conditionType; // 0(우대), 1(필수)
//	private int infoNo;
//	private String infoName;
	
	public ResumeCondition(int conditionNo, int resumeNo, int conditionType, int infoNo, int infoType, String infoName) {
		super(infoNo, infoType, infoName);
		this.conditionNo = conditionNo;
		this.resumeNo = resumeNo;
		this.conditionType = conditionType;
	}

	@Override
	public String toString() {
		return "ResumeCondition [conditionNo=" + conditionNo + ", resumeNo=" + resumeNo + ", conditionType="
				+ conditionType + ", infoNo=" + super.getInfoNo() + ", infoType=" + super.getInfoType() 
				+ ", infoName=" + super.getInfoName() + "]" ;
	}
}
