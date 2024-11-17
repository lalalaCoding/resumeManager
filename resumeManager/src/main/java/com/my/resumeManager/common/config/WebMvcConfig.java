package com.my.resumeManager.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	// 이미지 파일을 찾기 위해 매핑할 URI를 등록할 수 있는 메소드
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("file:///C:/resumeManager_downloadFiles/", "classpath:static/");
	}
}
