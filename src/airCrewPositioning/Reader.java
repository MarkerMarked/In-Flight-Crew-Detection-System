package airCrewPositioning;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;



public class Reader implements Runnable
{
// Program Variables ("instance variables")
Socket s;   
InputStream  ois;
double rssiValue;
public int flag;
private String ip;
//SwingWorkerRealTime readerGraph = new SwingWorkerRealTime();
//ObjectOutputStream oos;
// GUI objects
public static void main(String[] args) throws Exception
  {
  Reader reader1 = new Reader("192.168.1.10", 10004); // call CONSTRUCTOR
  Reader reader2 = new Reader("192.168.1.11", 10001);
  }
public Reader(String ipAddress, int port) throws Exception // CONSTRUCTOR
  {
    
  // First, a quick check of the parameters provided by our loading program:
  //new Reader(ipAddress, port);     
	ip  = ipAddress;
  System.out.println("Connecting to the Reader at " + ipAddress);
  s = new Socket(ipAddress, port);
  System.out.println("Connected to the reader!");
  //oos = new ObjectOutputStream(s.getOutputStream());
  //oos.writeObject(); // command line parms 1 & 2
  ois = (s.getInputStream()); // use for receiving
  System.out.println("Checkpoint");
  Thread t = new Thread(this); // create a Thread  
  t.start();     
  
  }
public void run() // ***RECEIVE***
  {
  char[] receivedData = new char[12];    //If you handle larger data use a bigger buffer size
  String receivedDataString = null;
  int row = 0;
  int col = 0;
  char[] rssi = new char[2]; 
  int receivedDataInt = 0;
  flag = 0;
  
  try {
    while(true) 
    {
        receivedData[col] = (char)ois.read();
        col++;
        if(col == 12)
        {
          flag = 1;
          System.out.println(receivedData);
          System.out.println(ip);
          rssi[0] = receivedData[1];
          rssi[1] = receivedData[2];
          receivedDataString = String.valueOf(rssi);
          receivedDataInt = Integer.parseInt(receivedDataString,16);
          rssiValue = receivedDataInt;
          //System.out.println(rssiValue);
          col = 0;
        }
          // Your code to handle the data
      }
    
} 
  
		  catch (IOException e) 
		  {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		  }
  }




}
