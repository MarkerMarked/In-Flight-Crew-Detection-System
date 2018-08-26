package airCrewPositioning;
import java.awt.Frame;

public class DrawGrids extends Frame {
      DrawGrids(String title, int w, int h, int rows, int columns) {
        setTitle(title);
        Grids grid = new Grids(w, h, rows, columns);
        add(grid);
      }
    }