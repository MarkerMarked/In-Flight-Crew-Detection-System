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
	static boolean DATASAVEENABLE = true;			//DataSave - Enable to save data to file (or console in devmode)
	static boolean DEVMODE = false;					//DevMode - Enable if running without a reader connection
	
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
    static int CREWHEIGHT = GRIDHEIGHT - (GRIDHEIGHT/6);
    static int CREWWIDTH = GRIDWIDTH - (GRIDWIDTH/6);
    
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
	    
	    //Running Loop
	    while (true) {
		    //Update Location
			if(DEVMODE == false) {
		    	ReceivedDataPacket packet = data.receive();
				if(crewMembers.containsKey(packet.tagID)){
					crewMembers.get(packet.tagID).updateLocation(locationCalculation(packet));
				}
			}

		    //Update and Save Crew Member Location
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
		crew.setSize(CREWWIDTH, CREWHEIGHT);
    	int convertedWidthLoc = (int) (crew.widthFtLocation * SIZERATIO) - CREWWIDTH/2;
    	int convertedHeightLoc = (int) (crew.heightFtLocation * SIZERATIO) - CREWHEIGHT/2;
    	
    	//Ensure crew is drawn inside the "aircraft"
    	if(convertedWidthLoc < 0) {
    		convertedWidthLoc = 0;
    	} else if(convertedWidthLoc > (DISPLAYWIDTH - CREWWIDTH)){
    		convertedWidthLoc = DISPLAYWIDTH - CREWWIDTH;
    	}
    	if(convertedHeightLoc < 0) {
    		convertedHeightLoc = 0;
    	} else if(convertedHeightLoc > (DISPLAYHEIGHT - CREWHEIGHT - (GRIDWIDTH/12))){
    		convertedHeightLoc = DISPLAYHEIGHT - CREWHEIGHT - (GRIDWIDTH/12);
    	}
    	
    	crew.setLocation(convertedWidthLoc + (GRIDWIDTH/12), convertedHeightLoc + (GRIDHEIGHT/12));
    	crewPane.add(crew, 0);
    }
    
    private void createNewSaveFile() {
    	try {
    		PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
    		if(DEVMODE == false) {
    			System.setOut(out);
    		}
    	} catch(IOException e1) {
    		System.out.println("Error during file creation");
    	}
    }
    
    private void saveCrewMemberLocation(CrewMember crew) {
    	if(DATASAVEENABLE) {
    		System.out.println("ID: " + crew.identifier + " X(WDTH): " + crew.widthFtLocation + " Y(HGHT): " + crew.heightFtLocation);
    	}
    }
    
    //Method checks to see if RSSI lengths are valid.
    //If they are, RSSI values are converted to Ft, and stored in packet.
    //If they are not, they are fixed. If they can't be fixed, valid flag is set to false and location is not updated.
    public ReceivedDataPacket locationCalculation(ReceivedDataPacket packet) {
    	packet.valid = true; //Set packet validity to true, if any part fails, set it to false and it will not be updated.
    	
    	packet.r1Ft = (packet.r1-30.5)/-1.2; //TEMP
        packet.r2Ft = (packet.r2-30.5)/-1.2; //TEMP
		
        if(packet.r1Ft+packet.r2Ft < 8) {
        	if (packet.r1Ft < packet.r2Ft) {
        		packet.r1Ft = 8 - packet.r2Ft;
        	}else {
        		packet.r2Ft = 8 - packet.r1Ft;
        	}
        }
        
        //Calculate X & Y Location
    	double x = (Math.pow(packet.r1Ft, 2) - Math.pow(packet.r2Ft, 2) + 64)/16;
		double y = Math.sqrt((Math.pow(packet.r1Ft, 2) - Math.pow(x, 2)));
		
		packet.r1Ft = x;
		packet.r2Ft = y;
		
		if(packet.r1Ft >= 0 && packet.r2Ft >= 0) {
			System.out.println("Negative Location Value Found");
			//May need to add code here to fix the "third case" where one R is too large, and the other R is too small they intersect outside the aircraft.
		}
		
		return packet;
	}
}
