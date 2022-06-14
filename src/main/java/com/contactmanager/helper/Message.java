package com.contactmanager.helper;

public class Message {
	
	private String messageContent;
	private String messageType;
	
	public Message() {
		super();
	}
	
	public Message(String messageContent, String messageType) {
		super();
		this.messageContent = messageContent;
		this.messageType = messageType;
	}

	public String getMessageContent() {
		return messageContent;
	}

	public void setMessageContent(String messageContent) {
		this.messageContent = messageContent;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Override
	public String toString() {
		return "Message [messageContent=" + messageContent + ", messageType=" + messageType + "]";
	}
	
	
}
