package com.module.apps;

import java.io.Serializable;

public class Message implements Serializable{
	private String senderHost;
	private int senderPort;
	private String msg;
	
	public Message(String senderHost, int senderPort, String msg)
	{
		this.senderHost = senderHost;
		this.senderPort = senderPort;
		this.msg = msg;
	}
	
	@Override
	public String toString() {
		return new String(senderHost+":"+senderPort+">>>"+msg);
	}
	
	public String getSenderHost() {
		return senderHost;
	}

	public void setSenderHost(String senderHost) {
		this.senderHost = senderHost;
	}

	public int getSenderPort() {
		return senderPort;
	}

	public void setSenderPort(int senderPort) {
		this.senderPort = senderPort;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
