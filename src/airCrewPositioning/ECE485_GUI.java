package airCrewPositioning;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class ECE485_GUI {
    
    JFrame crewWindow = new JFrame("Crew Positions");
    JPanel crewPanel  = new JPanel();
    Graphics g;
    
    public ECE485_GUI() {
        
         crewWindow.getContentPane().add(crewPanel, BorderLayout.CENTER);
           crewPanel.setBackground(Color.black);
           crewWindow.setSize(600,1300);
           crewWindow.setVisible(true);
           crewWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           g = crewPanel.getGraphics();
           Grids grid = new Grids(240, 900, 11 , 3);
           grid.setVisible(true);
           crewPanel.add(grid, BorderLayout.CENTER);
    }
    
    public static void main(String[] args) 
    {
        ECE485_GUI crewPosition = new ECE485_GUI ();
    }
    
}