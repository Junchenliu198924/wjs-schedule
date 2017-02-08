package com.wjs.schedule.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.wjs.schedule.enums.MessageType;

public class MessageInfo {
	
	private MessageType messageType;
	
	
	private Object message;


	public MessageType getMessageType() {
		return messageType;
	}


	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}


	public Object getMessage() {
		return message;
	}


	public void setMessage(Object message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
