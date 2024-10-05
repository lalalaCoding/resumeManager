package com.my.resumeManager.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.my.resumeManager.member.model.vo.Member;

@Mapper
public interface MemberMapper {

	int checkId(String memberId);

	int enrollMember(Member m);

	int enrollImage(Member m);

	Member login(Member m);
	
}
