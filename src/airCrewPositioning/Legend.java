package airCrewPositioning;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Hashtable;
import java.util.Map;

public class Legend extends Canvas {
	int width, height;
	Map<Integer, String> legendList = new Hashtable<Integer, String>();
	
	Legend(int w, int h) {
		setSize(width = w, height = h);
	}
	
	public void paint (Graphics g) {
		g.setColor(Color.green);
		int offset = 50;
		
		for (Map.Entry<Integer, String> entry : legendList.entrySet()) {
			String text = Integer.toString(entry.getKey()) + " - " + entry.getValue();
			g.drawString(text, this.getX(), this.getY()+offset);
			offset += 30;
		}
	}
	
	public void addNamePairing (int id, String name) {
		legendList.put(id, name);
	}
}
