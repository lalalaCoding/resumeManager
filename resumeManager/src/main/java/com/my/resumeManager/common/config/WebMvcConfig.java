package com.my.resumeManager.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.my.resumeManager.common.interceptor.AutoLoginInterceptor;
import com.my.resumeManager.common.interceptor.LoginCheckInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer{
	
	private final AutoLoginInterceptor autoLoginInterceptor;
	private final LoginCheckInterceptor loginCheckInterceptor;
	
	// 이미지 파일을 찾기 위해 매핑할 URI를 등록할 수 있는 메소드
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("file:///C:/resumeManager_downloadFiles/", "classpath:static/");
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//자동 로그인 인터셉터
		registry.addInterceptor(autoLoginInterceptor)
				.addPathPatterns("/*")
				.excludePathPatterns("/queue", "/topic", "/app", "ws-endpoint", "/logout", "/actuator/**", "/board/notice");
		
		//로그인 여부 인터셉터
		registry.addInterceptor(loginCheckInterceptor)
				.addPathPatterns("/*")
				.excludePathPatterns("/login", "/", "/actuator/**", "/board/notice");
		
		
		WebMvcConfigurer.super.addInterceptors(registry);
	}
	
	
}
