package com.module;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.StreamCorruptedException;
import java.net.Socket;
import static com.module.ModuleConstants.*;
import com.module.MessageListener;

public class MessageReceiver implements Runnable {
	private Socket clientSocket; //message is received with this socket
	private MessageListener messageListener; // message listener
	private boolean keepListening = true; //when false, ends listening
	private ObjectInputStream ois;

	/*
	 * Constructor method
	 */
	public MessageReceiver(MessageListener listener, Socket clientSocket) {
		this.messageListener = listener;
		this.clientSocket = clientSocket;
		
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream()); //gets input stream
		}catch (StreamCorruptedException e){
			System.out.print("");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This thread runs until connection is lost.
	 * It takes the message from input stream and sends it message listener.
	 */
	@Override
	public void run() {
		while (keepListening) {
			Object message = null; //creates incoming message object 
			try {
				message = ois.readObject(); //reads the input stream
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
			if (message != null) {
				if((message.toString()).equalsIgnoreCase(DISC_STR)) //if message is disconnect message
				{
					keepListening=false; //stop listening because client has been disconnected
				}
				else{
					//send message to message listener
					messageListener.onMessage(message);
					System.out.println();
					System.out.println("Incoming message: " + message.toString()); //writes incoming message to console instantly					
				}
			}
		}

		try {
			ois.close(); //closes the input stream - stop listening this socket
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		} // end catch
	}
}
