package com.my.resumeManager.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

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

	@Override
	public Member login(Member m) {
		return mMapper.login(m);
	}

	@Override
	public ArrayList<Member> checkNamePhone(Member m) {
		return mMapper.checkNamePhone(m);
	}

	@Override
	public Member selectMemberNo(int memberNo) {
		return mMapper.selectMemberNo(memberNo);
	}

	@Override
	public ArrayList<Member> checkIdPhone(Member m) {
		return mMapper.checkIdPhone(m);
	}

	@Override
	public int pwdModify(Member m) {
		return mMapper.pwdModify(m);
	}

	@Override
	public int updateImage(Member m) {
		return mMapper.updateImage(m);
	}

	@Override
	public int deleteImage(int memberNo) {
		return mMapper.deleteImage(memberNo);
	}

	@Override
	public int updateMember(HashMap<String, Object> editMap) {
		return mMapper.updateMember(editMap);
	}

	@Override
	public int quitMember(int memberNo) {
		return mMapper.deleteMember(memberNo);
	}
	
	
	
	
	
	
	
	
	
	
}
