package com.my.resumeManager.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.resumeManager.member.model.dao.MemberMapper;
import com.my.resumeManager.member.model.vo.Member;

@Service
public class MemberServiceImpl implements MemberService{
	@Autowired
	private MemberMapper mMapper;

	@Override
	public int checkId(String memberId) {
		return mMapper.checkId(memberId);
	}

	@Override
	public int enrollMember(Member m) {
		return mMapper.enrollMember(m);
	}

	@Override
	public int enrollImage(Member m) {
		return mMapper.enrollImage(m);
	}
	
	
	
	
	
	
	
	
	
	
}
