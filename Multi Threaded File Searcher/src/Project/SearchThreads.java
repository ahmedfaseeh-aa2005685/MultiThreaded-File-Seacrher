package Project;

import java.io.IOException;
import java.util.Scanner;

public class SearchThreads implements Runnable{
	
	static Object lock = new Object();
	
	static int fileIndex = 0;
	int totalFiles = ServiceServer.count;
	
	int fileperthread;
	int choice;
	int index;
	int tnum;
	int proc;
	
	public SearchThreads(int tnum, int choice, int fileperthread, int index, int proc) {
		this.choice = choice;
		this.fileperthread = fileperthread;
		this.index = index;
		this.tnum=tnum;
		this.proc = proc;
		if(fileIndex > proc)
			fileIndex = 0;
	}

	
	public void run() {
		
		try {
			ServiceServer.sem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		
		//Equal Distribution
		if(choice == 1) {
			
			long start = System.nanoTime();
			
			for(int x = 0; x < fileperthread; x++) {
				
				Process p;
				ProcessBuilder processBuilder = new ProcessBuilder();
				
				try {
					
					String filename = ServiceServer.files.get(index);
					
					processBuilder.command("bash", "-c", "grep -c -i " + ServiceServer.Userword + " " + ServiceServer.path + filename);
					p = processBuilder.start();
					Scanner reader = new Scanner(p.getInputStream());
					String occurance = reader.nextLine();
					
					
					if (Integer.parseInt(occurance) == 0) {
						String temp =  "  ThreadNumber = "+tnum+", Occurances: " + occurance + ", Filename: "+ filename;
						ServiceServer.checkedFiles.add(temp);
					}
					else {
						String temp =  "* ThreadNumber = "+tnum+", Occurances: " + occurance + ", Filename: "+ filename;
						ServiceServer.checkedFiles.add(temp);
					}
					reader.close();
					index++;
					
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
			
			long end = System.nanoTime();
			long time = (end-start)/1000000;
			
			ServiceServer.EDthreadTime[tnum-1] = "Thread " + tnum + " took " + time +"ms to execute";
			ServiceServer.EDsum+=time;
			
		}
		
		
		
		//Round Robin Distribution
		else {
			
			Process p;
			ProcessBuilder processBuilder = new ProcessBuilder();
			
			long start = System.nanoTime();
			
			while (true) {
				
	            synchronized (lock) {
	                if (fileIndex >= totalFiles) {
	                    break;
	                }
	                if ( fileIndex % proc == (tnum-1) ) {
	                	
						try {
							
							String filename = ServiceServer.files.get(fileIndex);
							processBuilder.command("bash", "-c", "grep -c -i " + ServiceServer.Userword + " " + ServiceServer.path + filename);
							p = processBuilder.start();
							Scanner reader = new Scanner(p.getInputStream());
							String occurance = reader.nextLine();
							
							if (Integer.parseInt(occurance) == 0) {
								String temp =  "  ThreadNumber = "+tnum+", Occurances: " + occurance + ", Filename: "+ filename;
								ServiceServer.checkedFiles.add(temp);
							}
							else {
								String temp =  "* ThreadNumber = "+tnum+", Occurances: " + occurance + ", Filename: "+ filename;
								ServiceServer.checkedFiles.add(temp);
							}
							
							reader.close();
							fileIndex++;
							
						} catch (IOException e) {
							e.printStackTrace();
						}
	                }
	                lock.notify();
//	                try {
//	                    if (fileIndex < ServiceServer.files.size()) {
//	                        lock.wait();
//	                    }
//	                } catch (InterruptedException e) {
//	                    System.out.println(e);
//	                }
	            }
	        }
			
			long end = System.nanoTime();
			long time = (end-start)/1000000;
			
			ServiceServer.threadTime[tnum-1] = "Thread " + tnum + " took " + time +"ms to execute";
			ServiceServer.RRsum+=time;
		}
		
		ServiceServer.sem.release();
		
	}
}








