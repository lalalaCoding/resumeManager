package com.my.resumeManager.member.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.resumeManager.common.gcs.GCSController;
import com.my.resumeManager.common.gcs.GCSRequest;
import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.member.model.vo.MemberException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {
	@Autowired
	private MemberService mService;
	
	@Autowired
	private GCSController gContoller;
	
	@Autowired
	private BCryptPasswordEncoder bCrypt;
	
	@GetMapping("enrollPage.me") // 회원가입 페이지로 이동
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
	public void enrollMember(@ModelAttribute Member m, @ModelAttribute GCSRequest profile, HttpServletResponse response) {
		m.setMemberPwd(bCrypt.encode(m.getMemberPwd())); // 비밀번호 암호화
		m.setMemberSocial('N'); // 소셜 로그인 유형 -> 임시로 N으로 지정하자. 소셜로그인을 구현할 때 수정해야함
		
		int enrollResult = mService.enrollMember(m);
		
		boolean result = false; // 가입 요청 결과가 성공인지 실패인지를 나타낼 플래그
		String msg = null; // 가입 요청 결과에 대한 안내문구를 저장할 문자열 선언 및 초기화
		try {
			if(!profile.getFile().isEmpty()) { // 첨부 파일의 용량이 0이 아닌 경우
				// GCS의 버킷에 저장할 파일 이름을 지정한다.
				String rename = getRename(profile.getFile());
				profile.setName(rename); 
				
				// DB에 저장할 이미지 파일 정보를 기록한다.
				m.setProfileOrigin(profile.getFile().getOriginalFilename()); // 원본 파일명 -> 추후에 다운로드를 제공할 때 이용가능함
				m.setProfileRename(rename); // 가공 파일명 -> 추후에 조회를 제공할 때 버킷에서 해당 파일을 찾을 수 있는 단서가 됨
				m.setProfilePath("C:\\resumeManager_downloadFiles"); // GCS -> 로컬에 저장한 파일을 불러올 때 사용한다.
				
				int imageResult = mService.enrollImage(m);
				
				if(imageResult > 0) { // 회원가입 성공
					result = true;
					gContoller.objectUpload(profile); // DB에 회원 정보 삽입이 성공했을 때만, GCS의 버킷에 업로드를 요청한다.
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
			
			if(result) { // 가입 요청 성공
				msg = "회원가입이 성공하였습니다.";
			} else {
				msg = "회원가입이 실패하였습니다.";
			}
			response.setContentType("text/html; charset=UTF-8");
			response.getWriter().write("<script>alert('" + msg +"'); location.href='/';</script>");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
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
	
	@GetMapping("loginPage.me")
	public String loginPage(@RequestParam(value="msg", required=false) String msg, HttpServletResponse response) {
		if(msg != null) {
			try {
				response.setContentType("text/html; charset=UTF-8"); // 유니코드 문자 집합을 UTF-8 방식으로 인코딩하도록 지정함
				response.getWriter().write("<script>alert('" + msg +"');</script>");
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return "member/login";
	}
	
	@PostMapping("login.me")
	public String login(@ModelAttribute Member m, HttpSession session, RedirectAttributes ra, HttpServletResponse response) {
		// id가 일치하는 회원을 조회
		Member loginMember = mService.login(m);
		String msg = null;
		
		if(loginMember != null) {
			if(bCrypt.matches(m.getMemberPwd(), loginMember.getMemberPwd())) { // 비밀번호 일치
				msg = loginMember.getMemberName() + "님, 로그인에 성공하였습니다.";
				session.setAttribute("loginMember", loginMember);
				try {
					response.setContentType("text/html; charset=UTF-8"); // 유니코드 문자 집합을 UTF-8 방식으로 인코딩하도록 지정함
					response.getWriter().write("<script>alert('" + msg +"'); location.href='resumeHistoryPage.rh';</script>");// URL이 'http://localhost:8080/' 표현되기 위함
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				msg = "회원 정보가 일치하지 않습니다.";
				ra.addAttribute("msg", msg);
				return "redirect:loginPage.me";
			}
		} else {
			msg = "회원 정보가 일치하지 않습니다.";
			ra.addAttribute("msg", msg);
			return "redirect:loginPage.me";
		}
		
		return "redirect:resumeHistoryPage.rh";
	}
	
	@GetMapping("findPage.me")
	public String findPage(@RequestParam("find") String find, HttpSession session) {
		if(find != null) {
			if(find.equals("id") || find.equals("pwd")) {
				session.setAttribute("find", find); // 정보를 찾는 대상이 아이디인지, 비밀번호인지를 구분하는 플래그를 세션에 저장한다. 
			} else {
				new MemberException("잘못된 요청입니다");
			}
		} else {
			new MemberException("잘못된 요청입니다.");
		}
		return "member/find";
	}
	
	@PostMapping("check-phone.me")
	@ResponseBody
	public Member checkPhone(@ModelAttribute Member m) {
		// 이름은 2글자 이상이지만 보통 3글자 이므로 9바이트의 크기를 갖는다.
		// 전화번호는 보통 11개의 숫자이므로 11바이트의 크기를 갖는다. 따라서 이름으로 조회한 이후에 전화번호를 컨트롤로에서 비교하는 것이 경제적이다.
		// Non Unique INDEX 사용 -> 회원 이름, 전화번호, 회원 번호를 조회한다.
		
		ArrayList<Member> searchMember = null;
		if(m.getMemberName() != null) {
			searchMember = mService.checkNamePhone(m);
		} else if(m.getMemberId() != null){
			searchMember= mService.checkIdPhone(m);
		} else {
			throw new MemberException("잘못된 요청입니다");
		}
		
		boolean result = false;
		Member findMember = null;
		for(Member mem : searchMember) { // 동일 이름을 갖는 회원 중 전화번호가 일치하는 회원이 존재하는지 확인
			if(mem.getMemberPhone().equals(m.getMemberPhone())) {
				result = true;
				findMember = mem;
				break;
			}
		}
		
		return findMember;
	}
	
	@PostMapping("find.me")
	public String findResult(@RequestParam("memberNo") int memberNo, @RequestParam("findInput") String find, Model model) {
		Member findMember = mService.selectMemberNo(memberNo);
		model.addAttribute("findMember", findMember);
		
		if(find.equals("id")) { // 아이디 찾기 결과 요청
			return "member/findIdResult";
		} else if(find.equals("pwd")) { // 비밀번호 찾기 결과 요청
			return "member/findPwdResult";
		} else {
			throw new MemberException("잘못된 요청입니다.");
		}
	}
	
	@PostMapping("pwdModify.me")
	public String pwdModify(@ModelAttribute Member m) {
		String newPwd = bCrypt.encode(m.getMemberPwd());
		m.setMemberPwd(newPwd);
		
		int result = mService.pwdModify(m);
		
		if(result == 1) {
			return "redirect:loginPage.me";
		} else {
			throw new MemberException("서비스 요청 장애가 발생하였습니다.");
		}
	}
	
	
	
	
}
