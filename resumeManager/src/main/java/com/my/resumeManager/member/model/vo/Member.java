package com.my.resumeManager.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Member extends ProfileImage{
	private String memberId;
	private String memberPwd;
	private String memberName;
	private char memberGender;
	private int mamgeAge;
	private String memberAddress;
	private String memberPhone;
	private char memberSocial;
	private char memberStatus;
	private String memberToken;
	private char memberType;
	private Date memberEnroll;
	private Date memberUpdate;
	private String memberEmail;
}
