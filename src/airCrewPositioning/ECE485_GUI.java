package airCrewPositioning;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ECE485_GUI {
    
    JFrame crewWindow = new JFrame("Crew Positions");
    JPanel inWindow = new JPanel(new FlowLayout());
    JLayeredPane crewPane = new JLayeredPane();
    JPanel legendPane = new JPanel();
    Grids grid;
    Legend legend;
    
    //PARAMETERS
    static int[] TAGIDS = new int[] {3584,3588};	//List of RFID tag TDs being used
    static int NUMBEROFTAGS = 2;		//Number of RFID tags being used
    
    static int HEIGHTFT = 30; 			// Height in Ft.
    static int WIDTHFT = 8;				// Width in Ft.
    static int SIZERATIO = 30;			// Ft. to Pix. Ratio
    static int ROWS = 15;				// Row Count
    static int COLUMNS = 4;				// Column Count
    static int DISPLAYHEIGHT = 1000;	// Display Window Height
    static int DISPLAYWIDTH = 600;		// Display Window Width
    static int GRIDWIDTH = WIDTHFT*SIZERATIO/COLUMNS;
    static int GRIDHEIGHT = HEIGHTFT*SIZERATIO/ROWS;
    
    private SynchDataPassthrough data = new SynchDataPassthrough();
   
    //Dictionary of Crew Member Tag IDs
    Map<Integer, CrewMember> crewMembers = new Hashtable<Integer, CrewMember>();
    
    public static void main(String[] args) throws Exception 
    {
        ECE485_GUI crewPosition = new ECE485_GUI ();
    }
    
    private ECE485_GUI() throws Exception{
    	//Connect Readers
    	Reader reader = new Reader("192.168.1.10", 10004, data);
    	Thread t = new Thread(reader); // create a Thread  
		t.start();
    	
    	//Fill CrewMembers Dictionary with CM Objects
    	for (int k = 0; k < NUMBEROFTAGS; k++) {
    		crewMembers.put(TAGIDS[k], new CrewMember(TAGIDS[k],0.0,0.0));
    	}
    	
    	//Point origin = new Point(10,10);
    	Dimension paneSize = new Dimension(WIDTHFT*SIZERATIO, HEIGHTFT*SIZERATIO);
    	
    	crewWindow.setContentPane(inWindow);
        crewWindow.getContentPane().add(crewPane);
        crewWindow.getContentPane().add(legendPane);
        crewWindow.getContentPane().setBackground(Color.black);
        crewPane.setBackground(Color.black);
        legendPane.setBackground(Color.black);
	    crewPane.setPreferredSize(paneSize);

	    legendPane.setPreferredSize(paneSize);
	    grid = new Grids(WIDTHFT*SIZERATIO, HEIGHTFT*SIZERATIO, ROWS, COLUMNS);
	    legend = new Legend(WIDTHFT*SIZERATIO, HEIGHTFT*SIZERATIO);
	    crewPane.add(grid, 1);
	    legendPane.add(legend);
	    crewWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    crewWindow.pack();
	    crewWindow.setVisible(true);
	    
	    //Create new data save file
	    createNewSaveFile();
	    
	//Complete Tag and Name Pairings
	legend.addNamePairing(3588, "Bob");
	legend.addNamePairing(3584, "Joe");
	
while (true) {	    
	    //Update Location
		
		ReceivedDataPacket packet = data.receive();
		
		System.out.println("ID: " + packet.tagID + " V1: " + packet.rssi1 + " V2: " + packet.rssi2);
	
		
		double xDist = 4;
		double hypo = Math.abs((packet.rssi1))-32;// + rssi2)/2 )-32;
		double yDist = (Math.pow(Math.pow(hypo, 2) - Math.pow(xDist, 2), .5))/1.5;
		
		int ID = packet.tagID;
		if(crewMembers.containsKey(ID)){
			crewMembers.get(ID).updateLocation(distanceConversion(packet.rssi1, packet.rssi2));
		}
		
		
	    //Display and Save Crew Member Location
	    for (int k = 0; k < NUMBEROFTAGS; k++) {
	        if (crewMembers.get(TAGIDS[k]).needsDisplay) {
	        	displayCrewMember(crewMembers.get(TAGIDS[k]));
	        	saveCrewMemberLocation(crewMembers.get(TAGIDS[k]));
	        	crewMembers.get(TAGIDS[k]).needsDisplay = false;
	        }
	    }

}
    }

    
    private void displayCrewMember(CrewMember crew) {
    	int convertedWidthLoc = (int) (crew.widthFtLocation * SIZERATIO);
    	int convertedHeightLoc = (int) (crew.heightFtLocation * SIZERATIO);
    	crew.setSize(GRIDWIDTH - (GRIDWIDTH/6), GRIDHEIGHT - (GRIDHEIGHT/6));
    	crew.setLocation(convertedWidthLoc + (GRIDWIDTH/12), convertedHeightLoc + (GRIDHEIGHT/12));
    	crewPane.add(crew, 0);
    }
    
    private void createNewSaveFile() {
    	try {

    		  PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
    		//System.setOut(out);
    		}
    		catch(IOException e1) {
    		        System.out.println("Error during reading/writing");
    		   }
    }
    
    private void saveCrewMemberLocation(CrewMember crew) {
    	
    }
 
    public Point2D.Double distanceConversion(double rssi1, double rssi2){
		
		double xDist = 4;
		double hypo = Math.abs((rssi1 + rssi2)/2 )-32;
		double yDist = (Math.pow(Math.pow(hypo, 2) - Math.pow(xDist, 2), .5))/1.5;
		
		return new Point2D.Double(xDist, yDist);
	}
}
