package airCrewPositioning;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
public class ECE485_GUI  implements Runnable  {
    
    JFrame crewWindow = new JFrame("Crew Positions");
    JPanel crewPanel  = new JPanel();
    Graphics g;
    
    public ECE485_GUI()
    {
        
         crewWindow.getContentPane().add(crewPanel, BorderLayout.CENTER);
           crewPanel.setBackground(Color.black);
           crewWindow.setSize(600,1300);
           crewWindow.setVisible(true);
           crewWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           g = crewPanel.getGraphics();
           Grids grid = new Grids(200, 1000, 40 , 8);
           grid.setVisible(true);
           crewPanel.add(grid, BorderLayout.CENTER);

    }
    
    public void run() 
    {
        while(true)
        {
        //System.out.print("Working");
        return;
        }
        
    }
    
    public static void main(String[] args) 
    {
        ECE485_GUI crewPosition = new ECE485_GUI ();
    }
    
}