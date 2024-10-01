package com.my.resumeManager.common;

// 임의의 인증번호 또는 비밀번호 생성해주는 메소드를 갖는 클래스 작성
public class RandomCreater {
	
	private static final String[] numberArr = {"0", "1", "2", "3", "4", "5", "6", "7", "8" ,"9"};
	
	private RandomCreater() {} // 싱글톤 패턴을 적용
	
	
	public static StringBuffer getRandomNumber(int digit) { // 자리 수를 매개변수로 전달받음
		StringBuffer sb = new StringBuffer("");
		for(int i = 0; i < digit; i++) {
			int random = (int)(Math.random()*10);
			sb.append(numberArr[random]);
		}
		return sb;
	}
	
}
