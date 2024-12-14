package com.my.resumeManager.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.annotation.security.PermitAll;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
        	.authorizeRequests()
        		.requestMatchers("/login", "/").permitAll() //로그인과 메인페이지에서만 접근을 허용함
        		.anyRequest().authenticated() //그 외 요청은 인증 필요
        		.and()
    		.formLogin()
    			.loginPage("/login") //로그인 페이지 경로 설정
    			.defaultSuccessUrl("/") //로그인 성공시 리다이렉트 URL
				.permitAll()
				.and()
			.httpBasic()
				.disable()
			.logout()
				.logoutSuccessUrl("/") //로그아웃 성공시 리다이렉트 URL
				.invalidateHttpSession(true)
				.and()
			.rememberMe() //Remember-me 기능 설정
				.rememberMeParameter("remember-me") // 기본 파라미터명 설정
	            .tokenValiditySeconds(604800) // 토큰 유효기간: 7일
	            .key("uniqueAndSecret"); // 보안 키
		
		return http.build();
	}

}
