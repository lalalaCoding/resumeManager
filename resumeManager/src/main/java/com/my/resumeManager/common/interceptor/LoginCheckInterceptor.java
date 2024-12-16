package com.my.resumeManager.common.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.my.resumeManager.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = request.getSession();
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('로그인 후 이용해주세요'); location.href='/login';</script>");
			return false;
		} else {
			return HandlerInterceptor.super.preHandle(request, response, handler);
		}
	}
}
