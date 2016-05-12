package com.module.apps;

import java.util.Scanner;

import com.module.SocketModule;
import static com.module.ModuleConstants.*;

public class SocketModuleTest {

	public static void main(String[] args) {
		// localPort=args[0] , host=args[1] , port=args[2]
		int localPort=Integer.parseInt(args[0]); //listens this port number as server
		String host = args[1]; //host-server address
		int port = Integer.parseInt(args[2]); //sends message from this port as client
		
		SocketModule sModule = new SocketModule(localPort,host,port);
		sModule.startServer(); //server started

		Scanner scanner = new Scanner(System.in);
		while (true) { //loop continues until application is closed
			while (true) { //loop continues until taking disconnect message from user
				System.out.print("Message: "); 

				String inputString = scanner.nextLine(); //message string value is taken from user

				if (inputString.equalsIgnoreCase(DISC_STR)) { //if string is disconnect message, 
					sModule.disconnect(); //disconnect method will be call
					break; //break from inner while
				} else if (inputString.equalsIgnoreCase(PRINT_STR)) //if string is print message, 
					printMessages(sModule); //print method will be call
				else {
					Message msg = new Message(host,localPort,inputString); //message object that will be send is created with sender info
					sModule.sendMessage(msg); //sends the message
					System.out.println();
				}
			}
			
			String msg2;
			do {
				System.out.println("Type \"CONNECT\" command to continue or exit program... ");
				System.out.print(">");
				msg2 = scanner.nextLine();
				if (msg2.equalsIgnoreCase(PRINT_STR)) 
					printMessages(sModule); 
			} while (!msg2.equalsIgnoreCase(CON_STR)); //until taking connect string, input string will be taken from the user
			
			sModule.doConnect(); //opens the connection with server to send message
		}	
	}
	
	/*
	 * gets all messages in the queue, prints them on console and removes from the queue
	 */
	public static void printMessages(SocketModule sModule)
	{
		Object msg;
		System.out.println("List of all received messages:");
		
		if(sModule.getMessageQueueSize()==0) //if there is no message in the message queue
			System.out.println("There are no messages to be displayed.");
		else{
			while((msg=sModule.getMessage())!=null) //gets message from the queue until it is empty
			{
				System.out.println(msg.toString()); //prints the message to the console
			}			
		}
	}
}
