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
    height = getSize().height ;
    //horizontal lines
    int htOfRow = height / (rows);
    for (k = 0; k <= (rows); k++)
      g.drawLine(0, k * htOfRow , width, (k * htOfRow));
    //vertical Lines
    int wdOfRow = width / (columns);
    for (k = 0; k <= (columns+4); k++)
      g.drawLine(k*wdOfRow , 0, k*wdOfRow , height);
    
    
    
  }

}