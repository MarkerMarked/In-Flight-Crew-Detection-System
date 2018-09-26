package airCrewPositioning;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class ECE485_GUI {
    
    JFrame crewWindow = new JFrame("Crew Positions");
    JPanel crewPanel  = new JPanel();
    JLayeredPane crewPane = new JLayeredPane();
    Grids grid;
    
    //CONSTANTS
    static int HEIGHTFT = 30; 			// Height in Ft.
    static int WIDTHFT = 8;				// Width in Ft.
    static int SIZERATIO = 30;			// Ft. to Pix. Ratio
    static int ROWS = 15;				// Row Count
    static int COLUMNS = 4;				// Column Count
    static int DISPLAYHEIGHT = 1000;	// Display Window Height
    static int DISPLAYWIDTH = 600;		// Display Window Width
    static int GRIDWIDTH = WIDTHFT*SIZERATIO/COLUMNS;
    static int GRIDHEIGHT = HEIGHTFT*SIZERATIO/ROWS;
    
    public static void main(String[] args) 
    {
        ECE485_GUI crewPosition = new ECE485_GUI ();
    }
    
    private ECE485_GUI() {
        crewWindow.getContentPane().add(crewPane, BorderLayout.CENTER);
        crewPane.setBackground(Color.black);
	    crewWindow.setSize(DISPLAYWIDTH,DISPLAYHEIGHT);
	    crewWindow.setVisible(true);
	    crewWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    grid = new Grids(WIDTHFT*SIZERATIO, HEIGHTFT*SIZERATIO, ROWS, COLUMNS);
	    crewPane.add(grid, 1);
	    
	    //Crew Member Testing Code - ALL DISTANCES FROM TOP LEFT CORNER
        CrewMember firstCrewMember = new CrewMember(0, 6.0, 4.0);
        displayCrewMember(firstCrewMember);
    }
    
    private void displayCrewMember(CrewMember crew) {
    	int convertedWidthLoc = (int) (crew.widthFtLocation * SIZERATIO);
    	int convertedHeightLoc = (int) (crew.heightFtLocation * SIZERATIO);
    	crew.setSize(GRIDWIDTH, GRIDHEIGHT);
    	crew.setLocation(convertedWidthLoc, convertedHeightLoc);
    	crewPane.add(crew, 0);
    }
    
}
