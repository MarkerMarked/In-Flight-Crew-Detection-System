package airCrewPositioning;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;
import java.math.BigInteger;

public class Reader implements Runnable {
	// Program Variables ("instance variables")
	Socket s;   
	InputStream  ois;
	double rssiValue1 = 0;
	double rssiValue2 = 0;
	private String ip;
	int tagID = 0;
	
	private SynchDataPassthrough data;// = new SynchDataPassthrough();
	
	public static void main(String[] args) throws Exception {
	 	Reader reader = new Reader("192.168.1.10", 10004, new SynchDataPassthrough());
	}
	
	public Reader(String ipAddress, int port, SynchDataPassthrough datathrough) throws Exception { // CONSTRUCTOR 
		ip  = ipAddress;
		System.out.println("Connecting to the Reader at " + ipAddress);
		s = new Socket(ipAddress, port);
		System.out.println("Connected to the reader!");
		ois = (s.getInputStream()); // use for receiving
		data = datathrough;
		//Thread t = new Thread(this); // create a Thread  
		//t.start();     
	  
	}
	
	public void run() { // ***RECEIVE***
	  char[] receivedData = new char[12];    //If you handle larger data use a bigger buffer size
	  String receivedDataString = null;
	  
	  char[] receivedData2 = new char[12];    //If you handle larger data use a bigger buffer size
	  String receivedDataString2 = null;
	  
	  int col = 0;
	  char[] rssi = new char[2]; 
	  char[] activatorid1 = new char[2];
	  char[] activatorid2 = new char[2];
	  
	  String tagIDString = null;
	  char[] tagIDChar = new char[3];
	  
	  int receivedDataInt = 0;
	  int receivedDataInt2 = 0;
	  
	  try {
	    while(true) {
	        receivedData[col] = (char)ois.read();
	        col++;
	        if(col == 12) {
	        	rssi[0] = receivedData[7];
	        	rssi[1] = receivedData[8];
	        	activatorid1[0] = receivedData[6];
	        	activatorid1[1] = receivedData[7];
		        receivedDataString = String.valueOf(rssi);
				receivedDataString = hexToBin(receivedDataString);
				receivedDataString = String.format("%8s", receivedDataString).replace(" ", "0");
				receivedDataString = receivedDataString.substring(3,8);
				
				receivedDataString2 = String.valueOf(activatorid1);
				receivedDataString2 = hexToBin(receivedDataString2);
				receivedDataString2 = String.format("%8s", receivedDataString2).replace(" ", "0");
				receivedDataString2 = receivedDataString2.substring(1,7);
				
				receivedDataInt = Integer.parseInt(receivedDataString,2);
				receivedDataInt2 = Integer.parseInt(receivedDataString2,2);
				
				tagIDChar[0] = receivedData[3];
				tagIDChar[1] = receivedData[4];
				tagIDChar[2] = receivedData[5];
				tagIDString = String.valueOf(tagIDChar);
		//System.out.println("Hex: " + tagIDString);
				tagIDString = hexToBin(tagIDString);
				tagIDString = String.format("%8s", tagIDString).replace(" ", "0");
				tagID = Integer.parseInt(tagIDString,2);
		//System.out.println("Dec: " + tagID);
				
				
				if(receivedDataInt2 == 2) {
					rssiValue1 = receivedDataInt;
				}
				
				if(receivedDataInt2 == 4) {
					rssiValue2 = receivedDataInt;
				}
		//System.out.println("1: " + rssiValue1);
		//System.out.println("2: " + rssiValue2);
				
				System.out.println("SEND");
				data.send(new ReceivedDataPacket(tagID, rssiValue1, rssiValue2));
				
				col = 0;
	        }
	        
	      }
	  }
	  
	  catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	  }
	  }
	
	static String hexToBin(String s) {
		  return new BigInteger(s, 16).toString(2);
	}
	
	public synchronized void receiveNewReading() {
		int Test;
	}
	
	public synchronized void updateTagInfo() {
		int Test;
	}
	
}