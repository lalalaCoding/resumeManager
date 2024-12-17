package com.my.resumeManager.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

//@Component
//@Aspect
@Slf4j
public class AutoLoginAop {
	
//	@Before("execution(* com.my.resumeManager..*.*(..))")
//	public void autoLogin(JoinPoint joinPoint) {
//		//로그인 쿠키가 있는지 확인 : 
//		log.info("HELLO AOP!");
//
//	}
}
