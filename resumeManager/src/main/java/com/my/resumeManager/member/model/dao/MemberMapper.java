package com.my.resumeManager.member.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.my.resumeManager.member.model.vo.Member;

@Mapper
public interface MemberMapper {

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

	int updateMember(@Param("editMap")HashMap<String, Object> editMap);

	int deleteMember(int memberNo);
	
}
