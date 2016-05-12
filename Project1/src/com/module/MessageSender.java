package com.module;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class MessageSender implements Runnable {
	private Object message; //message to be sent
	private ObjectOutputStream oos; //output stream to send message(object type)
	
	public MessageSender(Object message, ObjectOutputStream oos) {
		this.message = message;
		this.oos = oos;
	}
	
	@Override
	public void run() {
		try {
			oos.writeObject(message); //writes message to stream
			oos.flush();
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println("Server is disconnected.");
		}
	}
}
