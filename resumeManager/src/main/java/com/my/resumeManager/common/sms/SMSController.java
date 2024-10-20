package com.my.resumeManager.common.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.my.resumeManager.common.CompanyInfo;

import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@RestController
public class SMSController {
	// 필드
	final DefaultMessageService messageService;
	
	@Autowired
	private CompanyInfo companyInfo;
	
	// 생성자
	public SMSController(@Value("${sms.API_KEY}") String API_KEY, @Value("${sms.API_SECRET_KEY}") String API_SECRET_KEY) {
		//this.messageService = NurigoApp.INSTANCE.initialize("INSERT_API_KEY", "INSERT_API_SECRET_KEY", "https://api.coolsms.co.kr");
		//this.messageService = NurigoApp.INSTANCE.initialize("NCSQT3RFQTKQ71BH", "UW0EFHNCDP7BNMQRCIH0YYHWX1BQ9NJW", "https://api.coolsms.co.kr");
		this.messageService = NurigoApp.INSTANCE.initialize(API_KEY, API_SECRET_KEY, "https://api.coolsms.co.kr");
	}
	
	
	// 단일 메시지 발송
	@PostMapping("/send-one") // 010에서 맨 앞의 0때문에 문자열로 전달받아야함
    public SingleMessageSentResponse sendOne(@RequestParam("phoneNumber") String phoneNumber, HttpSession session) { 
        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom(companyInfo.getCompanyPhone());
        message.setTo(phoneNumber);
        String code = RandomCreater.getRandomNumber(6).toString();
        
       	session.setAttribute("code", code); // 세션에 인증 번호를 저장한다.
        System.out.println("세션의 코드 번호 " + session.getAttribute("code"));
        String msg = "인증 번호 " + code;
        message.setText(msg);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);
        
        return response;
		
    }
	
	// 세선에 저장된 코드 값 반환
	@PostMapping("get-code")
	public String getCode(HttpSession session) {
		return (String)session.getAttribute("code");
	}
	
	
	
	
	
	
}
