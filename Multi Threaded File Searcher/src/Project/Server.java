package Project;

import java.io.*;
import java.net.*;

public class Server {
	public Server(){
		try{
			
			@SuppressWarnings("resource")
			ServerSocket server= new ServerSocket(4000);
			System.out.println("Server is up, waiting for Connections on port "+server.getLocalPort());
			Socket client;
			
			
			for(;;){
				client=server.accept();
				new ServiceServer(client).start();
			}
			
		}catch(IOException ioe){
			System.out.println("I/O Error related:"+ioe);
		}
	}
	public static void main (String args[]){
		new Server();
	}
}





