package com.my.resumeManager.member.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.resumeManager.common.gcs.GCSController;
import com.my.resumeManager.common.gcs.GCSRequest;
import com.my.resumeManager.member.model.service.MemberService;
import com.my.resumeManager.member.model.vo.Member;
import com.my.resumeManager.member.model.vo.MemberException;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class MemberController {
	@Autowired
	private MemberService mService;
	
	@Autowired
	private GCSController gController;
	
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
	public void enrollMember(@ModelAttribute Member m, @ModelAttribute GCSRequest profile, HttpServletResponse response, @RequestParam("memberAge") int memberAge) {
		m.setMemberPwd(bCrypt.encode(m.getMemberPwd())); // 비밀번호 암호화
		m.setMemberSocial('N'); // 소셜 로그인 유형 -> 임시로 N으로 지정하자. 소셜로그인을 구현할 때 수정해야함
		
		log.info("생년월일={}", memberAge);
		log.info("가입정보={}", m);
		
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
					gController.objectUpload(profile); // DB에 회원 정보 삽입이 성공했을 때만, GCS의 버킷에 업로드를 요청한다.
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
		log.info("loginMember={}", loginMember);
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
	
	@GetMapping("infoPage.me")
	public String infoPage(@RequestParam("info") String info, Model model, HttpSession session) {
		model.addAttribute("info", info); //인포 페이지에서 메뉴바의 클래스명을 제어하기 위한 정보
		
		Member loginMember = (Member) session.getAttribute("loginMember");
		int memberNo = 0;
		if (loginMember != null) {
			memberNo = loginMember.getMemberNo();
		} else {
			throw new MemberException("로그인 후 이용해주세요.");
		}
		
		log.info("로그인 멤버={}", loginMember);
		
		if (info.equals("general")) {
			//destFilePath=C:\resumeManager_downloadFiles, objectName=1731636787493_80976.jpg
			HashMap<String, String> profileMap = new HashMap<>();
			if (loginMember.getProfileOrigin() != null) { //프로필이 등록된 회원인 경우
				profileMap.put("objectName", loginMember.getProfileRename());
				profileMap.put("destFilePath", loginMember.getProfilePath());
				
				log.info("프로필 다운로드 정보={}", profileMap);
				
				File myProfile = new File(profileMap.get("destFilePath") + "/" + profileMap.get("objectName"));
				log.info("파일존재={}", myProfile.exists());
				
				if (!myProfile.exists()) { //로컬에 프로필 사진이 저장되어 있지 않음
					gController.objectDownload(profileMap);
				}
			}
			
			return "member/generalInfo";
		}
		
		
		if (info.equals("pwd")) {
			return "member/modifyPwd";
		}
		
		
		throw new MemberException("서비스 요청에 실패하였습니다.");
	}
	
	@GetMapping("members/{memberNo}/edit")
	public String editMemberPage(@PathVariable("memberNo") int memberNo, @RequestParam("info") String info, HttpSession session, Model model) {
		log.info("회원 번호={}",memberNo);
		//로그인 여부 -> 요청 회원번호와 로그인한 회원번호 일치 여부
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) { //로그인 x
			throw new MemberException("로그인 후 이용해주세요.");
		} else if (loginMember.getMemberNo() != memberNo) { //로그인o, 본인 조회 요청x
			throw new MemberException("잘못된 요청입니다.");
		} else {
			if (info.equals("general")) { //회원 일반정보 수정 폼으로 이동
				model.addAttribute("info", info);
				return "member/generalEditInfo";
			} else if (info.equals("pwd")) { //회원 비밀번호 수정 폼으로 이동
				
				
				
				return null;
			} else {
				throw new MemberException("잘못된 요청입니다.");
			}
			
		}
	}
	
	
	//members/{memberNo}/edit?info=general
	@PostMapping("members/{memberNo}/edit")
	public String editMember(@PathVariable("memberNo") int memberNo, @RequestParam("info") String info, HttpSession session, Model model,
								@ModelAttribute Member m, @ModelAttribute GCSRequest profile, @RequestParam("changeObj") String changeObj,
								RedirectAttributes ra) {
		log.info("회원 수정정보={}", m);
		log.info("프로필 수정정보={}", profile);
		log.info("변경 정보={}", changeObj);
		
		boolean editFlag = false; //수정 성공 여부를 판단해줄 플래그
		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			throw new MemberException("로그인 후 이용해주세요.");
		} else if (loginMember.getMemberNo() != memberNo) {
			throw new MemberException("잘못된 요청입니다.");
		} else {
			m.setMemberNo(memberNo);
			m.setMemberId(loginMember.getMemberId());
		}
		
		//로그인o && 수정하려는 회원 번호 == 로그인된 회원 번호
		//jacksondatabind : 변경 정보를 Map으로 가공
		ObjectMapper mapper = new ObjectMapper();
		Map<String,Boolean> changeMap = null;
		try {
			changeMap = mapper.readValue(changeObj, new TypeReference<HashMap<String,Boolean>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		log.info("changeMap={}", changeMap);
		log.info("changeMap2={}", changeMap.get("profileChange"));
		
		if (changeMap.get("profileChange")) { //프로필 변경o : MultipartFile 객체 자체는 무조건 생성되어 넘어옴
			//기존 프로필 정보o : gcs -> 삭제
			log.info("프로필 유무={}", loginMember.getProfileRename());
			boolean oldProfileExist = loginMember.getProfileRename() == null ? false : true;
			
			if (oldProfileExist) { //기존 프로필 정보가 존재함
				gController.objectDelete(loginMember.getProfileRename()); //GCS에서 기존 프로필 개체 삭제
				deleteFile(loginMember);//로컬 폴더에서 기존 프로필 파일 삭제 (파일 저장위치와 파일 이름이 필요함)
			}
			
			MultipartFile file = profile.getFile();
			if (!file.isEmpty()) { //프로필 교체하기 요청
				// GCS의 버킷에 저장할 파일 이름을 지정한다.
				String rename = getRename(file);
				profile.setName(rename); 
				
				// DB에 저장할 이미지 파일 정보를 기록한다.
				m.setProfileOrigin(profile.getFile().getOriginalFilename()); // 원본 파일명 -> 추후에 다운로드를 제공할 때 이용가능함
				m.setProfileRename(rename); // 가공 파일명 -> 추후에 조회를 제공할 때 버킷에서 해당 파일을 찾을 수 있는 단서가 됨
				m.setProfilePath("C:\\resumeManager_downloadFiles"); // GCS -> 로컬에 저장한 파일을 불러올 때 사용한다.
				
				//기존 프로필이 존재o : 프로필 테이블 업데이트
				//기존 프로필이 존재x : 프로필 테이블 삽입
				int imageResult = 0;
				if (oldProfileExist) {
					imageResult = mService.updateImage(m); //db -> update
				} else {
					imageResult = mService.enrollImage(m); //db -> insert
				}
				
				log.info("프로필 수정 결과={}", imageResult);
				
				if (imageResult > 0) {
					try {
						gController.objectUpload(profile); //gcs -> 삽입
						editFlag = true;
					} catch (IOException e) {
						editFlag = false;
						e.printStackTrace();
					}
				}
			} else { //프로필 제거하기 요청
				log.info("프로필 유무={}", loginMember.getProfileRename());
				if (oldProfileExist) { //기존의 프로필 정보가 존재
					int imageResult = mService.deleteImage(memberNo); //db -> 삭제
					deleteFile(loginMember);//로컬 폴더에서 기존 프로필 파일 삭제 (파일 저장위치와 파일 이름이 필요함)
					editFlag = imageResult > 0 ? true : false;
				}
				//기존의 프로필 정보가 존재x : 아무것도 할 필요가 없음
			}
		} 
		
		
		//세션에 있는 로그인 정보를 최신화
		Member updateLoginMember = mService.login(m);
		session.setAttribute("loginMember", updateLoginMember);
		
		ra.addAttribute("info", "general");
		return "redirect:/infoPage.me";
	}
	
	public boolean deleteFile(Member loginMember) {
		String dir = loginMember.getProfilePath(); //삭제할 파일의 디렉토리
		String fileName = loginMember.getProfileRename(); //삭제할 파일의 이름
		
		log.info("삭제 파일 경로={}", dir + "/" + fileName);
		File file = new File(dir + "/" + fileName);
		if (file.exists()) { //로컬에 삭제 대상인 파일이 존재하는 경우
			return file.delete();
		} else { //로컬에 삭제 대상인 파일이 존재하지 않는 경우
			return true;
		}
	}
	
	
	
	
	
	
	
	
	
	//실험실
	@GetMapping("srcTest")
	public String srcTest1() {
		return "member/generalInfo2";
	}
	
	@GetMapping("src/Test")
	public String srcTest2() {
		return "member/generalEditInfo2";
	}
	
	
	
}
