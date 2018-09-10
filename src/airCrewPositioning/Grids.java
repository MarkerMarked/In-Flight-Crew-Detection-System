package airCrewPositioning;

import java.awt.*;

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
    width = getSize().width;
    height = getSize().height;
    
    //Draw outside rectangle
    g.drawRoundRect(1, 1, width-2, height-2, 25, 25);
    
    //Draw horizontal lines
    int htOfRow = height / rows;
    for (k = 1; k <= rows; k++)
      g.drawLine(0, k*htOfRow , width, k*htOfRow);
    
    //Draw vertical lines
    int wdOfRow = width / columns;
    for (k = 1; k <= columns; k++)
      g.drawLine(k*wdOfRow , 0, k*wdOfRow , height);
    
  }
}