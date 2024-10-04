package com.my.resumeManager.member.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Member extends ProfileImage{
	private int memberNo;
	private String memberId;
	private String memberPwd;
	private String memberName;
	private char memberGender;
	private int memberAge;
	private String memberAddress;
	private String memberPhone;
	private char memberSocial;
	private char memberStatus;
	private String memberToken;
	private char memberType;
	private char memberHistory;
	private Date memberEnroll;
	private Date memberUpdate;
	private String memberEmail;
}
