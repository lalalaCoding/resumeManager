package com.my.resumeManager.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper {

	int checkId(String memberId);
	
}
