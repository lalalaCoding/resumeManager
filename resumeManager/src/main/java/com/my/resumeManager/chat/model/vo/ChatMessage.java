package com.my.resumeManager.chat.model.vo;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ChatMessage extends ChatMember{
	private int messageNo;
	private String messageContent;
	private int messageCount;
	private Date messageCreate;
	private char messageStatus;
}
