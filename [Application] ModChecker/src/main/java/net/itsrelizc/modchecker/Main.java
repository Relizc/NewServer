package net.itsrelizc.modchecker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.server.TerminalServer;

public class Main {
    
    public static void main(String[] args) {
    	
//    	boolean gui = true;
//    	for (String s : args) {
//    		if (s.equalsIgnoreCase("-nogui")) {
//    			gui = false;
//    		}
//    	}
//    	if (gui) {
//    		
//            
//            
//    	}
    	
    	try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Enigma [Deployed]");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        
        JTextArea textArea = new JTextArea("Enigma 1.0.3\n");
        
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12)); // Windows default font
        textArea.setPreferredSize(new Dimension(600, 250));
        
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(textArea, BorderLayout.NORTH);
        frame.add(panel);
        
        frame.setVisible(true);
        frame.pack();
        
        
        
        RelizcLogger.areaLogger = textArea;
        textArea.setEditable(false);
//        
//        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
//        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        
        
        panel.add(scroll);
        
        
    	RelizcLogger.info("Enigma: 1.0.3 Not Equipped with Anticheat");
    	RelizcLogger.info("Runtime arguments: [" + String.join(", ", args) + "]");
    	
    	
    	
    	
    	int i = 0;
    	for (String s : args) {
    		if (s.equalsIgnoreCase("-modsDirectory")) {
        		TerminalServer.currentDir = args[i + 1];
        	}
    		if (s.equalsIgnoreCase("-dir")) {
        		System.setProperty("user.dir", args[i + 1]);
        	}
    		if ("-server".equalsIgnoreCase(s)) {
    			
    			RelizcLogger.info("Launching server...");
    			TerminalServer.main(args);
    			RelizcLogger.info("Server closed");
    			
    		} else if ("-client".equalsIgnoreCase(s)) {
    			
    			
    			
    			RelizcLogger.info("Launching client...");
    			TerminalClient.main(args);
    			
    		}
    		i ++;
    	}
    	
    	
    	
    }
}
