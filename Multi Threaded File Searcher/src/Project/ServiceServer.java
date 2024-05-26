package Project;

import java.io.*;


import java.net.*;
import java.util.*;
import java.util.concurrent.Semaphore;

@SuppressWarnings("unused")
public class ServiceServer extends Thread{
	
	public static int count;
	public static String path;
	public static String Userword;
	public static ArrayList<String> files = new ArrayList<String>();
	public static ArrayList<String> checkedFiles = new ArrayList<String>();
	public static String[] threadTime;
	public static int RRsum;
	public static Semaphore sem;
	
	public static String[] EDthreadTime;
	public static int EDsum;
	
	
	Socket client;
	 
	public ServiceServer(Socket c){
		client=c;
	}
	
	public void run(){
		try{
			
			
			Process p;
			PrintWriter output= new PrintWriter(client.getOutputStream(),true);
			Scanner input= new Scanner(client.getInputStream());
			ProcessBuilder processBuilder = new ProcessBuilder();
			
			
			
			//Calculating the number of processors
			processBuilder.command("bash", "-c", "nproc");
			p = processBuilder.start();
			Scanner reader = new Scanner(p.getInputStream());
			String numofp = reader.nextLine();
			System.out.println();
			System.out.println("This PC has "+ numofp + " processors");
			reader.close();
			
			
			
			//Checking the OS of the system
			processBuilder.command("bash", "-c", "uname -o");
            p = processBuilder.start();
            reader = new Scanner(p.getInputStream());
            
            String OS = reader.nextLine(); //This has the name of the OS
            String[] osname;
            osname = OS.split("/");
            System.out.println("This OS is: " + osname[1]);
            reader.close();
            
            
            
            Thread.sleep(200);
            //Sending The GUI CommandLine-Interface
			output.println("Welcome to Parallel File Search Server Load Distributor!\n"
					+ "Choose one of the following options:\n"
					+ "1. Equal Distribution.\n"
					+ "2. Round Robin Distribution\n"
					+ "3. Quit");
			
			
			
			//Getting the users response
			String x = input.nextLine();
			int choice = Integer.parseInt(x);
			
			if(choice == 3) {
				client.close();
				System.exit(0);
			}
			
			
			
			//Getting directory and word from user
			output.println("Enter The File Directory (Make sure it ends in '/'): ");
			String directoryPath = input.nextLine();
			path = directoryPath;
			output.println("Enter The Word You Want To Search: ");
			String word = input.nextLine();
			Userword = word;
			

			
			//Calculating number of files in the directory
            processBuilder.command("bash", "-c", "cd " + directoryPath + " && ls -1 *.txt | wc -l");
            p = processBuilder.start();
            reader = new Scanner(p.getInputStream());
            
            String line = reader.nextLine(); //This has the number of files in that directory
            count = Integer.parseInt(line); //This makes count the number of files
            reader.close();
            
            
            
            //Getting the files 
            processBuilder.command("bash", "-c", "ls " + directoryPath + "*.txt");
            p = processBuilder.start();
            reader = new Scanner(p.getInputStream());
            
            String[] lol = new String[count]; 
            for(int l = 0; l < count; l++) {
            	String filepath = reader.nextLine();
            	lol = filepath.split("/");
            	files.add(lol[lol.length-1]);
            }
            reader.close();
            
            
            
            //Creating threads from no. of processors
            int proc = Integer.parseInt(numofp);
            sem = new Semaphore(proc);
            threadTime = new String[proc];
            EDthreadTime = new String[proc];
            int fileperthread = count/proc;
            int index = 0;
            int tnum;
            int remain = count%proc;
            int totalthreadnum = 0;
            Thread t;
            
			for (int i=0;i < proc;i++){
				tnum = i+1;
				
				if(remain != 0) {
					t = new Thread(new SearchThreads(tnum, choice,fileperthread+1,index, proc));
					remain--;
					index+=fileperthread+1;}
				else {
					t = new Thread(new SearchThreads(tnum, choice,fileperthread,index, proc));
					index+=fileperthread;}
				
				t.start();
			}
			
			
			
			//Add RoundRobin time into the CheckedFiles Array
			sem.acquire(proc);
			if(choice == 2) {
				for(int i = 0; i < threadTime.length; i++) {
					checkedFiles.add(threadTime[i]);
				}
				checkedFiles.add("");
				checkedFiles.add("Total thread execution time: " + RRsum + "ms");
			}
			
			else {
				for(int i = 0; i < EDthreadTime.length; i++) {
					checkedFiles.add(EDthreadTime[i]);
				}
				checkedFiles.add("");
				checkedFiles.add("Total thread execution time: " + EDsum + "ms");
			}
			
			
			
			//Send the length of the array
			output.println(checkedFiles.size());
			
			
			
			//Sending the array elements 1 by 1
			for(String sr: checkedFiles) {
				output.println(sr);
			}
			
			
			
			//Initialize everything back to 0
			RRsum = 0;
			count = 0;
			path = "";
			files = new ArrayList<String>();
			checkedFiles = new ArrayList<String>();
			EDsum = 0;
			sem.release(proc);
		}
		catch (IOException ioe) {
			System.err.println(ioe);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}









