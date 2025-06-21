package net.itsrelizc.directrix.window;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTree;

public class Panel {
	
	private static Panel instance;
	
	private static void setInstance(Panel ins) {
		instance = ins;
	}
	
	public static Panel getInstance() {
		return instance;
	}
	
	JFrame frame;
	JTree nodes;
	GridLayout experimentLayout;
	
	DNode parent;
	
	public Panel() {
		
		experimentLayout = new GridLayout(1, 1);
		
		frame = new JFrame("Directrix v2.0.1");
		frame.setLayout(experimentLayout);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		parent = new DNode();
		nodes =  new JTree(parent);
		frame.add(nodes);

        frame.setVisible(true);
	}

    public static void main(String[] args) {
        
    	Panel panel = new Panel();
    	setInstance(panel);
    	
    }
}
