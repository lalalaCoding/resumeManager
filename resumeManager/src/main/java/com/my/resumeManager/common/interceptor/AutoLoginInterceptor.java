package com.my.resumeManager.common.interceptor;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.my.resumeManager.member.controller.MemberController;
import com.my.resumeManager.member.model.dao.MemberMapper;
import com.my.resumeManager.member.model.vo.Member;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AutoLoginInterceptor implements HandlerInterceptor{
	
	private final MemberMapper mMapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		log.info("Hello AutoLoginInterceptor");
		
		boolean cookieExist = false;
		Member loginMember = null;
		HttpSession session = request.getSession();
		
		Cookie[] myCookies = request.getCookies();
		if (myCookies != null) {
			for (Cookie c : myCookies) {
				if (c.getName().equals("remember-me")) {
					HashMap<String, Object> condition = new HashMap<>();
					condition.put("rememberFlag", true);
					condition.put("memberNo", Integer.parseInt(c.getValue()));
					log.info("login cookieName={}", c.getName());
					log.info("login cookieValue={}", c.getValue());
					cookieExist = true;
					loginMember = mMapper.selectOneMember(condition);
					break;
				}
			}
		} else {
			return true;
		}
		
		if (cookieExist) {
			if (loginMember != null) {
				log.info("loginMember={}",loginMember);
				session.setAttribute("loginMember", loginMember);
				return HandlerInterceptor.super.preHandle(request, response, handler);
			} else {
				MemberController.deleteCookie("remember-me", null, response);
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().write("<script>alert('존재하지 않는 회원입니다.'); location.href='/login';</script>");
				
				return false;
			}
		} else {
			return HandlerInterceptor.super.preHandle(request, response, handler);
		}
		
	}
}
