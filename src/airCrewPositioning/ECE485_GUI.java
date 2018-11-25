package airCrewPositioning;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ECE485_GUI {
	//OPTIONS:
	static boolean DATASAVEENABLE = true;
	static boolean DEVMODE = true;
	
    //PARAMETERS
    static int[] TAGIDS = new int[] {3584,3588};	//List of RFID tag TDs being used
    static int NUMBEROFTAGS = 2;					//Number of RFID tags being used
    static int HEIGHTFT = 30; 						// Height in Ft.
    static int WIDTHFT = 8;							// Width in Ft.
    static int SIZERATIO = 30;						// Ft. to Pix. Ratio
    static int ROWS = 15;							// Row Count
    static int COLUMNS = 4;							// Column Count
    static int DISPLAYHEIGHT = 1000;				// Display Window Height
    static int DISPLAYWIDTH = 600;					// Display Window Width
    static int GRIDWIDTH = WIDTHFT*SIZERATIO/COLUMNS;
    static int GRIDHEIGHT = HEIGHTFT*SIZERATIO/ROWS;
    
    Map<Integer, CrewMember> crewMembers = new Hashtable<Integer, CrewMember>();
    private SynchDataPassthrough data = new SynchDataPassthrough();
    JFrame crewWindow = new JFrame("Crew Positions");
    JPanel inWindow = new JPanel(new FlowLayout());
    JLayeredPane crewPane = new JLayeredPane();
    JPanel legendPane = new JPanel();
    Grids grid;
    Legend legend;
    
    public static void main(String[] args) throws Exception 
    {
        ECE485_GUI crewPosition = new ECE485_GUI ();
    }
    
    private ECE485_GUI() throws Exception{
    	//Connect Reader
    	if(DEVMODE == false) {
    		Reader reader = new Reader("192.168.1.10", 10004, data);
    		Thread t = new Thread(reader); // create a Thread  
    		t.start();
    	}
    	
    	//Fill CrewMembers Dictionary with all crew
    	for (int k = 0; k < NUMBEROFTAGS; k++) {
    		crewMembers.put(TAGIDS[k], new CrewMember(TAGIDS[k],0.0,0.0));
    	}
    	
    	//Complete Tag and Name Pairings
    	legend = new Legend(WIDTHFT*SIZERATIO, HEIGHTFT*SIZERATIO);
    	legend.addNamePairing(3588, "Bob");
    	legend.addNamePairing(3584, "Joe");
    	
    	//Create new data save file
	    createNewSaveFile();
	    
	    //Window Creation and Configuration
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
	    crewPane.add(grid, 1);
	    legendPane.add(legend);
	    crewWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    crewWindow.pack();
	    crewWindow.setVisible(true);
	    
	    while (true) {	    
		    //Update Location
			if(DEVMODE == false) {
		    	ReceivedDataPacket packet = data.receive();
				
				int ID = packet.tagID;
				if(crewMembers.containsKey(ID)){
					crewMembers.get(ID).updateLocation(distanceConversion(packet.rssi1, packet.rssi2));
				}
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
    		if(DEVMODE == false) {
    			System.setOut(out);
    		}
    	}
    	catch(IOException e1) {
    		System.out.println("Error during file creation");
    	}
    }
    
    private void saveCrewMemberLocation(CrewMember crew) {
    	if(DATASAVEENABLE) {
    		System.out.println("ID: " + crew.identifier + " X(WDTH): " + crew.widthFtLocation + " Y(HGHT): " + crew.heightFtLocation);
    	}
    }
 
    public Point2D.Double distanceConversion(double rssi1, double rssi2){
		double xDist = 4;
		double hypo = Math.abs((rssi1 + rssi2)/2 )-32;
		double yDist = (Math.pow(Math.pow(hypo, 2) - Math.pow(xDist, 2), .5))/1.5;
		return new Point2D.Double(xDist, yDist);
	}
}
