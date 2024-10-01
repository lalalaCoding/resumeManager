package com.my.resumeManager.common;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 프로젝트 배포자에 대한 정보를 저장하고 있는 클래스
@NoArgsConstructor
@Setter
@Getter
@Component
public class CompanyInfo {
	private static final String COMPANY_INFO = "01071430231";
	
	
	public String getCompanyPhone() {
		return COMPANY_INFO;
	}
}
