package com.my.resumeManager.common.interceptor;

import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.my.resumeManager.member.controller.MemberController;
import com.my.resumeManager.member.model.dao.MemberMapper;
import com.my.resumeManager.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@Transactional
public class AutoLoginInterceptorTest {
	
	@Autowired MemberMapper mMapper;
	
	@Test
	public void 회원번호조회_실패() {
		//given
		int memberNo = 100000;
		HashMap<String, Object> condition = new HashMap<>();
		condition.put("rememberFlag", true);
		condition.put("memberNo", memberNo);
		
		//when
		Member loginMember = null;
		loginMember = mMapper.selectOneMember(condition);
		
		//then
		Assertions.assertThat(loginMember).isNull();
	}
	
	
	
	
	
	
}
