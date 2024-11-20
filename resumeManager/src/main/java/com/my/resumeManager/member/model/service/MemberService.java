package com.my.resumeManager.member.model.service;

import java.util.ArrayList;

import com.my.resumeManager.member.model.vo.Member;

public interface MemberService {

	int checkId(String memberId);

	int enrollMember(Member m);

	int enrollImage(Member m);

	Member login(Member m);

	ArrayList<Member> checkNamePhone(Member m);

	Member selectMemberNo(int memberNo);

	ArrayList<Member> checkIdPhone(Member m);

	int pwdModify(Member m);

	int updateImage(Member m);

	int deleteImage(int memberNo);

}
