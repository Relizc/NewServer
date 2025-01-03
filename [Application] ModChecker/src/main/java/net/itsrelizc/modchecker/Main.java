package net.itsrelizc.modchecker;

import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Main {
	
	public static Logger LOGGER = Logger.getLogger("RelizcModChecker");
	
	public static void main(String[] args) {
		LOGGER.info("Relizc SMP Mod Checker 1.0.0");
		LOGGER.info("Current time: " + System.currentTimeMillis());
		
		JFrame frame = new JFrame("Simple GUI Window");
        
        // Set the size of the frame
        frame.setSize(400, 300);
        
        // Specify the action when the window is closed
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a label to display some text
        JLabel label = new JLabel("Hello, World!", SwingConstants.CENTER);
        
        // Add the label to the frame
        frame.add(label);
        
        // Make the frame visible
        frame.setVisible(true);
	}
	
}
