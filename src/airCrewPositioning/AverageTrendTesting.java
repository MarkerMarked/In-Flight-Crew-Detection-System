package airCrewPositioning;

import java.io.*;

import java.net.Socket;

public class AverageTrendTesting implements Runnable {
	Socket s;   
	InputStream  ois;

	public static void main(String[] args) throws Exception {
		new AverageTrendTesting(); 	// call CONSTRUCTOR
	}
	
	public AverageTrendTesting() throws Exception {		// CONSTRUCTOR
		System.out.println("Connecting to the Reader at 192.168.1.10");
		s = new Socket("192.168.1.10", 10001);
		System.out.println("Connected to the reader!");
		ois = (s.getInputStream()); // use for receiving
		Thread t = new Thread(this); // create a Thread  
		t.start();     
	}
	
	public void run() { // ***RECEIVE***
		  
		  char[][] receivedData = new char[5][12];
		  String [] receivedDataString = new String[5];
		  int[] receivedDataInt = new int[5];
		  int row = 0;
		  int col = 0;
		  double dist = 0;
		  double [] rssiTest = new double[10];
		  int counter = 0;
		  double sum = 0;
		          
		  char [][] last5RSSI = new char[5][2];
		  try {
		    while(true) 
		    {
		        
		        
		        receivedData[row][col] = (char)ois.read();
		        col++;
		        if(col == 12)
		        {
		         
		          System.out.print(receivedData[row]);
		          last5RSSI[row][0] = receivedData[row][1];
		          last5RSSI[row][1] = receivedData[row][2];
		          receivedDataString[row] = String.valueOf(last5RSSI[row]);
		          receivedDataInt[row] = Integer.parseInt(receivedDataString[row],16);
		          rssiTest[counter] = receivedDataInt[row];
		          System.out.println(receivedDataInt[row]);
	
		          row++;
		          counter++;
		          if(counter == 10)
		          {
		              for(int c = 0; c<10; c++)
		              {
		                  sum += rssiTest[c];
		              }
		              dist = sum/10;
		              System.out.println(dist);
		              return;
		          }
		          
		          col = 0;
		        }
		        if(row == 5)
		        {
		            row = 0;
		        }
		      }
		  } catch (IOException e) {
			  e.printStackTrace();
		  	}
	  }
}
