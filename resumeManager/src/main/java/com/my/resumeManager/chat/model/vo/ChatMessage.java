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
public class ChatMessage extends ChatMember{
	private int messageNo;
	private String messageContent;
	private int messageCount;
	private Date messageCreate;
	private char messageStatus;
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChatMessage [messageNo=").append(messageNo).append(", messageContent=").append(messageContent)
				.append(", messageCount=").append(messageCount).append(", messageCreate=").append(messageCreate)
				.append(", messageStatus=").append(messageStatus)
				.append(", roomVisiterId=").append(super.getRoomVisiterId())
				.append(", joinNo=").append(super.getJoinNo()).append("]");
		return builder.toString();
	}
	
	
	
}
