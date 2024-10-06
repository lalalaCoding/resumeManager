package com.my.resumeManager.member.model.service;

import com.my.resumeManager.member.model.vo.Member;

public interface MemberService {

	int checkId(String memberId);

	int enrollMember(Member m);

	int enrollImage(Member m);

	Member login(Member m);

	Member checkNamePhone(Member m);

}
