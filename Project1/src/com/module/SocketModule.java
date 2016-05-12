package com.module;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.module.ModuleConstants.*;
public class SocketModule implements MessageListener{
	private int localPort; // port no. for listening message as server
	private String host; // host-server address
	private int port; // port no. for sending message as client
	
	private Queue<Object> messageQueue; //incoming message queue
	
	private ServerSocket server; // ServerSocket object for incoming connections(requests)
	private Socket socket; //Socket object on the server
	private ObjectOutputStream oos; //this stream for sending message as client
	private ExecutorService serverExecutor; //to execute threads
	
	private boolean hasConnected = false; //status flag to check connection with server
	
	/*
	 * Constructor method
	 */
	public SocketModule(int localPort, String host, int port) {
		this.localPort = localPort;
		this.port = port;
		this.host = host;
		messageQueue = new LinkedList<Object>();
		serverExecutor = Executors.newCachedThreadPool();
	}

	/*
	 * Start the server thread and listen for other application(s) from determined port(localPort) 
	 */
	public void startServer() {
		try {
			server = new ServerSocket(localPort);
			System.out.println("Server is now listening "+localPort+" port.");
			serverExecutor.execute(new ServerThread(server,this)); //runs ServerThread thread to listen connections and handle events
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Object message) {
		if(!hasConnected) //we need to be connected with server before send message as client
			doConnect();
		if(hasConnected)
			serverExecutor.execute(new MessageSender(message,oos)); //runs MessageSender thread to send message
	}

	/*
	 * Add new message to end of the message queue
	 */
	public void addMessage(Object message) {
		messageQueue.add(message);
	}
	
	/*
	 * Removes the first item in the queue and returns it
	 */
	public Object getMessage() {
		if(messageQueue.peek()!=null)
			return messageQueue.remove();
		else return null;
	}
	
	/*
	 * Returns the number of messages in the queue
	 */
	public int getMessageQueueSize() {
		return messageQueue.size();
	}
	
	/*
	 * Before send the message, we need to connect other client
	 */
	public void doConnect()
	{
		try {
			socket = new Socket(InetAddress.getByName(host),port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			hasConnected = true; //status flag is updated
		} catch (ConnectException e){
			System.out.println("ServerSocket is not open.");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	/*
	 * Accepts a message as a parameter and processes it
	 */
	@Override
	public void onMessage(Object message) {
		messageQueue.add(message); //adds incoming message to message queue.
	}
	
	/*
	 * When application sends the disconnect message, connection is terminated with server.
	 */
	public void disconnect() 
	{
		if(hasConnected)
		{
			serverExecutor.execute(new MessageSender(DISC_STR,oos));
			hasConnected=false; //connection is closed with server
		}
	}
}
