package com.my.resumeManager.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.member.model.dao.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberMapper mMapper;

	@Override
	public int checkId(String memberId) {
		return mMapper.checkId(memberId);
	}
	
	
	
	
	
	
	
	
	
	
}
