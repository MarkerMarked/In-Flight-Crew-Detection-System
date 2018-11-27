package airCrewPositioning;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;
import java.math.BigInteger;

public class Reader implements Runnable {
	// Program Variables ("instance variables")
	Socket s;   
	InputStream  ois;
	double rssiValue1 = 0;
	double rssiValue2 = 0;
	int tagID = 0;
	
	private SynchDataPassthrough data;// = new SynchDataPassthrough();
	
	public static void main(String[] args) throws Exception {
	 	Reader reader = new Reader("192.168.1.10", 10004, new SynchDataPassthrough());
	}
	
	public Reader(String ipAddress, int port, SynchDataPassthrough datathrough) throws Exception { // CONSTRUCTOR 
		System.out.println("Connecting to the Reader at " + ipAddress);
		s = new Socket(ipAddress, port);
		System.out.println("Connected to the reader!");
		ois = (s.getInputStream()); // use for receiving
		data = datathrough;
	}
	
	public void run() { // ***RECEIVE***
		
		Map<Integer, Double> rssi1Tracker = new Hashtable<Integer, Double>();
		Map<Integer, Double> rssi2Tracker = new Hashtable<Integer, Double>();
		
		char[] receivedData = new char[12];    //If you handle larger data use a bigger buffer size
		String receivedDataString = null;
		  
		String receivedDataString2 = null;
		  
		int col = 0;
		char[] rssi = new char[2]; 
		char[] activatorid1 = new char[2];
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
			
					tagIDString = hexToBin(tagIDString);
					tagIDString = String.format("%8s", tagIDString).replace(" ", "0");
					tagID = Integer.parseInt(tagIDString,2);
			
					if(receivedDataInt2 == 2) {
						if (rssi1Tracker.containsKey(tagID)) {
							rssi1Tracker.replace(tagID, (double) receivedDataInt);
						} else {
							rssi1Tracker.put(tagID, (double) receivedDataInt);
						}
					}
					
					if(receivedDataInt2 == 4) {
						if (rssi2Tracker.containsKey(tagID)) {
							rssi2Tracker.replace(tagID, (double) receivedDataInt);
						} else {
							rssi2Tracker.put(tagID, (double) receivedDataInt);
						}
						
					}
					
					if (rssi1Tracker.containsKey(tagID) && rssi2Tracker.containsKey(tagID)) {
						data.send(new ReceivedDataPacket(tagID, rssi1Tracker.get(tagID), rssi2Tracker.get(tagID)));
						rssi1Tracker.remove(tagID);
						rssi2Tracker.remove(tagID);
					}
					col = 0;
		        }    
		    }
		 } catch (IOException e) {
			 e.printStackTrace();
		 }
	}
		
	static String hexToBin(String s) {
		  return new BigInteger(s, 16).toString(2);
	}
}