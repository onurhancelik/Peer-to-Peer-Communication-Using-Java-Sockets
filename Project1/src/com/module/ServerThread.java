package com.module;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerThread implements Runnable {
	private ServerSocket server;
	private ExecutorService serverExecutor;
	private MessageListener messageListener; // receives messages

	public ServerThread(ServerSocket server, MessageListener listener) {
		this.server = server;
		this.messageListener = listener;
		serverExecutor = Executors.newCachedThreadPool();
	}

	/*
	 * ServerThread will listen for connections and runs the MessageReceiver to handle message.
	 * This operation has to happen on a separate thread because it is always running.
	 */
	@Override
	public void run() {
		try {
			while (true) {
				Socket clientSocket = server.accept(); //accepts incoming connections and creates client socket

				System.out.println("Connection was established with client.");

				serverExecutor.execute(new MessageReceiver(messageListener, clientSocket)); //runs the MessageReceiver thread to handle incoming message
			}

		} catch (SocketException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
