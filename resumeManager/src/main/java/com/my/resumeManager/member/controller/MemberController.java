package com.my.resumeManager.member.controller;

import java.io.IOException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.my.resumeManager.common.gcs.GCSController;
import com.my.resumeManager.common.gcs.GCSRequest;
import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class MemberController {
	@Autowired
	private MemberService mService;
	
	@Autowired
	private GCSController gContoller;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	
	@GetMapping("header.test")
	public String headerTest() {
		return "common/header";
	}
	
	@GetMapping("footer.test")
	public String footerTest() {
		return "common/footer";
	}
	
	@GetMapping("enroll.me") // 회원가입 페이지로 이동
	public String enrollPage() {
		return "member/enroll";
	}
	
	@GetMapping("checkId.me")
	@ResponseBody
	public String checkId(@RequestParam("memberId") String memberId) {
		int result = mService.checkId(memberId);
		return result == 0 ? "yes" : "no";
	}
	
	@PostMapping("enrollMember.me")
	public String enrollMember(@ModelAttribute Member m, @ModelAttribute GCSRequest profile, HttpServletResponse response) {
		m.setMemberPwd(bCrypt.encode(m.getMemberPwd())); // 비밀번호 암호화
		m.setMemberSocial('N'); // 소셜 로그인 유형 -> 임시로 N으로 지정하자. 소셜로그인을 구현할 때 수정해야함
		int enrollResult = mService.enrollMember(m);
		
		boolean result = false; // 가입 요청 결과가 성공인지 실패인지를 나타낼 플래그
		
		try {
			if(!profile.getFile().isEmpty()) { // 첨부 파일의 용량이 0이 아닌 경우
				// GCS의 버킷에 저장할 파일 이름을 지정한다.
				String rename = getRename(profile.getFile());
				profile.setName(rename); 
				
				// DB에 저장할 이미지 파일 정보를 기록한다.
				m.setProfileOrigin(profile.getFile().getOriginalFilename()); // 원본 파일명 -> 추후에 다운로드를 제공할 때 이용가능함
				m.setProfileRename(rename); // 가공 파일명 -> 추후에 조회를 제공할 때 버킷에서 해당 파일을 찾을 수 있는 단서가 됨
				m.setProfilePath("C:\\resumeManager_downloadFiles"); // GCS -> 로컬에 저장한 파일을 불러올 때 사용한다.
				 
				
				gContoller.objectUpload(profile); // GCS의 버킷에 업로드를 요청한다.
				
				int imageResult = mService.enrollImage(m);
				
				if(imageResult > 0) { // 회원가입 성공
					result = true;
				} else { // 회원가입 실패
					result = false;
				}
			} else {
				if(enrollResult > 0) { // 회원가입 성공
					result = true;
				} else { // 회원가입 실패
					result = false;
				}
			}
			  
			String msg = null; // 가입 요청 결과에 대한 안내문구를 저장할 문자열 선언 및 초기화
			response.setContentType("text/html; charset=UTF-8"); // 가입 요청 결과를 출력하기 위한 컨텐츠 타입을 지정함
			if(result) { // 가입 요청 성공
				msg = "회원가입이 성공하였습니다.";
				response.getWriter().write("<script>alert('" + msg + "');</script>");
			} else {
				msg = "회원가입이 실패하였습니다.";
				response.getWriter().write("<script>alert('" + msg + "');</script>");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "index";
	}
	
	public String getRename(MultipartFile file) {
		System.out.println("원본 파일 명 : " + file.getOriginalFilename()); // IMG_3914.JPG
		
		// 파일 이름을 생성하기 -> "현재시각을 밀리초로 변환" + "_" + "랜덤숫자" + 확장자명
		Calendar now = Calendar.getInstance();
		long millisecond = now.getTimeInMillis(); // 밀리초 계산
		int random = (int)(Math.random()*100000); // 10만 이하의 랜덤 숫자 생성
		
		// 확장자 알아내기
		String originalName = file.getOriginalFilename();
		int dotIndex = originalName.lastIndexOf("."); // 파일명에서 마지막 . 의 인덱스 찾기
		String fileType = originalName.substring(dotIndex); // ".jpg"와 같은 형태
		return millisecond + "_" + random + fileType; // "현재시각에 대한 밀리초_랜덤숫자.확장자"
	}
	
	
	
	
}
