package com.my.resumeManager.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

@Controller // 공통적인 요청을 처리할 컨트롤러 작성
public class CommonController {
	
	@PostMapping("sessionRemoveAttribute.cm")
	@ResponseBody
	public String sessionRemoveAttribute(@RequestParam("name") String name, HttpSession session) {
		Object obj = null;
		if(name != null) {
			session.removeAttribute(name);
			obj = session.getAttribute(name);
		}
		
		return obj == null ? "success" : "fail";
	}
}
