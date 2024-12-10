package com.my.resumeManager.member.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;

@SpringBootTest
public class MemberControllerTest {
	
	@Autowired BCryptPasswordEncoder bCrypt;
	@Autowired MemberService mService;
	
	@Test
	public void 회원탈퇴_유효성검사() {
		Member loginMember = mService.selectMemberNo(22);
		
		boolean idCheck = false;
		boolean pwdCheck = false;
		String memberId = "test005";
		String memberPwd = "!@id235689";
		
		//아이디, 비밀번호
		if (loginMember.getMemberId().equals(memberId)) {
			idCheck = true;
		}
		if (bCrypt.matches(memberPwd, loginMember.getMemberPwd())) {
			pwdCheck = true;
		}
		
		Assertions.assertThat(idCheck).isEqualTo(true);
		Assertions.assertThat(pwdCheck).isEqualTo(true);
	}
	
	
	
	
}
