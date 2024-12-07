package com.my.resumeManager.chat.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.my.resumeManager.common.gcs.GCSController;
import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;

@SpringBootTest
public class ChatControllerTest {
	
	@Autowired private GCSController gController;
	
	@Autowired private MemberService mService;
	
	
	
	@Test
	public void 프로필_다운로드() {
		//case
		int numberFlag = 0;
		Member sender = mService.selectMemberNo(10); //로그인 회원 가정
		Member receiver = mService.selectMemberNo(7);
		
		//when
		ArrayList<Member> chatVisiters = new ArrayList<>(List.of(sender, receiver));
		
		for (Member m : chatVisiters) {
			if (m.getProfileOrigin() != null) { //프로필이 등록된 회원인 경우
				HashMap<String, String> profileMap = new HashMap<>();
				profileMap.put("objectName", m.getProfileRename());
				profileMap.put("destFilePath", m.getProfilePath());
				
				File myProfile = new File(profileMap.get("destFilePath") + "/" + profileMap.get("objectName"));
				
				if (!myProfile.exists()) { //로컬에 프로필 사진이 저장되어 있지 않음
					gController.objectDownload(profileMap);
				}
				
				if (myProfile.exists()) { //로컬에 파일 다운로드가 성공함
					numberFlag++;
				}
				
			} else {
				numberFlag++; //프로필이 등록되지 않은 회원인 경우
			}
		}
		
		//then
		Assertions.assertThat(numberFlag).isEqualTo(2);
	}
	
	
	
	
	
}
