package com.my.resumeManager.resumeHistory.model.vo;

import java.sql.Date;

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
public class ResumeHistory{
	private int resumeNo; // 이력 번호
	private int resumeWriter; // 지원자 번호
	private Date resumeDate; // 지원 일자
	private String companyName; // 회사 명
	private int companySalary; // 급여 수준
	private String companyRegion; // 회사 위치
	private int resumeState; // 지원상태 -> 0(지원), 1(서류합격), 2(면접합격), 3(최종합격) 
	private int resumeCarrer; // 경력
	private Date resumeDeadline; // 지원마감일 resumeDeadline
	private CompanyType companyType; // 회사 직군 객체 -> SI, SM, ...
	private PlatformType platform; // 지원 플랫폼 객체 -> 인크루트, 잡코리아, ...
}
