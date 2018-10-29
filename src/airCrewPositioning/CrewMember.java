package airCrewPositioning;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class CrewMember extends Canvas {
	int identifier;
	double widthFtLocation, heightFtLocation;
	boolean needsDisplay;
	
	//Constructor - Takes ID, and location (from top left corner in ft.)
	public CrewMember(int id, double wFt, double hFt) {
		identifier = id;
		widthFtLocation = wFt;
		heightFtLocation = hFt;
		needsDisplay = true;
	}

	//Update current location (from top left corner in ft.)
	public void updateLocation(double wFt, double hFt) {
		widthFtLocation = wFt;
		heightFtLocation = hFt;
		needsDisplay = true;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.green);
	    g.drawOval(0, 0, getSize().width-1, getSize().height-1);
	    g.drawString(Integer.toString(identifier), getSize().width/2, getSize().height/2);
	}
}
