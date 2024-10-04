package com.my.resumeManager.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration // 설정에 관련한 클래스임을 알려주는 어노테이션
public class SecurityConfig {
	@Bean // 메소드의 리턴 값을 Bean으로 처리하도록 하는 어노테이션 
	BCryptPasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
