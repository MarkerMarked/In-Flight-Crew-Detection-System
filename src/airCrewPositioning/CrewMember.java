package airCrewPositioning;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Point2D;

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
	public void updateLocation(Point2D.Double location) {
		widthFtLocation = location.getX();
		heightFtLocation = location.getY();
		needsDisplay = true;
	}
	
	public void updateLocation(double x, double y) {
		widthFtLocation = x;
		heightFtLocation = y;
		needsDisplay = true;
	}

	public void updateLocation(ReceivedDataPacket packet) {
		if (packet.valid) {
			widthFtLocation = packet.r1Ft;
			heightFtLocation = packet.r2Ft;
			needsDisplay = true;
		}
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.green);
	    g.drawOval(0, 0, getSize().width-1, getSize().height-1);
	    g.drawString(Integer.toString(identifier), getSize().width/3, getSize().height*3/5);
	}
}
