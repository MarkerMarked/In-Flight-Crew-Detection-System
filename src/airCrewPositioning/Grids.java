package airCrewPositioning;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

class Grids extends Canvas {
	int width, height, rows, columns;
  
	Grids(int w, int h, int r, int c) {
		setSize(width = w, height = h);
		rows = r;
		columns = c;
	}
  
	public void paint(Graphics g) {
	    int k;
	    g.setColor(Color.green);
	    int drawableWidth = getSize().width-1;
	    int drawableHeight = getSize().height-1;
	    
	    //Draw outside rectangle
	    g.drawRoundRect(0, 0, drawableWidth, drawableHeight, 25, 25);
	    
	    //Draw horizontal lines
	    int heigtOfRow = height / rows;
	    for (k = 1; k <= rows; k++)
	    	g.drawLine(0, k*heigtOfRow , width, k*heigtOfRow);
	    
	    //Draw vertical lines
	    int widthOfRow = width / columns;
	    for (k = 1; k < columns; k++)
	    	g.drawLine(k*widthOfRow , 0, k*widthOfRow , height);
	}
	
}

