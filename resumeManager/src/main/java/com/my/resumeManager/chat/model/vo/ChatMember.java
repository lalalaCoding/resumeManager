package com.my.resumeManager.chat.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChatMember extends ChatRoom{
	
	private int joinNo;
	private int roomVisiter;
	private String roomVisiterId;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ChatMember [joinNo=").append(joinNo).append(", roomVisiter=").append(roomVisiter)
				.append(", roomVisiterId=").append(roomVisiterId)
				.append(", roomNo=").append(super.getRoomNo())
				.append(", roomStatus=").append(super.getRoomStatus()).append("]");
		return builder.toString();
	}
}
