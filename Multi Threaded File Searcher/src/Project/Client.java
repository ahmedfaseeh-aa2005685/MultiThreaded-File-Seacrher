package Project;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	public Client(){
		
		
		Socket client = null;

		
		try{
			client = new Socket("localhost",4000);

			Scanner input= new Scanner(client.getInputStream());
			Scanner keyboard=new Scanner(System.in);
			PrintWriter output= new PrintWriter(client.getOutputStream(),true);
				

			
			System.out.println();
			//Getting the Interface
			System.out.println(input.nextLine());
			System.out.println(input.nextLine());
			System.out.println(input.nextLine());
			System.out.println(input.nextLine());
			System.out.println(input.nextLine());
			
			
			
			//Sending the Users command
			String choice = keyboard.nextLine();
			output.println(choice);
			if(Integer.parseInt(choice) == 3)
				System.exit(0);
			
			
			
			System.out.println();
			//Sending the directory to search
			System.out.println(input.nextLine());
			output.println(keyboard.nextLine());
			System.out.println(input.nextLine());
			output.println(keyboard.nextLine());
			
			
			
			//Getting the length of the array
			String z = input.nextLine();
			int len = Integer.parseInt(z);
			System.out.println();
			System.out.println("--------------------------------------------------------");
			if(Integer.parseInt(choice) == 1)
				System.out.println("Thread Information after Equal Distribution:\n");
			else
				System.out.println("Thread Information after Round Robin:\n");
			
			
			
			//Printing the Array
			for(int x = 0; x < len; x++) {
				System.out.println(input.nextLine());
			}
			System.out.println("--------------------------------------------------------");
				
			
			
			keyboard.close();
			input.close();	
			client.close();
			
		}catch(IOException ioe){
			System.out.println("IO related error "+ioe);
		}
	}
	public static void main(String srgs[]){
		new Client();
	}
}
