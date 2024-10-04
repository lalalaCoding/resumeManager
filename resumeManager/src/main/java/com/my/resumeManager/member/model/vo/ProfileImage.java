package com.my.resumeManager.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProfileImage {
	private int memberNo;
	private String profileOrigin;
	private String profileRename;
	private String profilePath;
}
