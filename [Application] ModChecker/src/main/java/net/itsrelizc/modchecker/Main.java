package net.itsrelizc.modchecker;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;

import net.itsrelizc.nettyservice.Client;
import net.itsrelizc.nettyservice.Downloader;

public class Main {
    
    public static void main(String[] args) {

        JFrame frame = new JFrame("Enigma [Deployed]");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setLocationRelativeTo(null);

        JTextArea textArea = new JTextArea("Enigma 1.0.3\n");
        RelizcLogger.areaLogger = textArea;
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.setEditable(false);

        // Auto-scroll
        DefaultCaret caret = (DefaultCaret) textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        // Wrap in scroll pane
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scroll, BorderLayout.CENTER); // Add scroll pane directly
        frame.setVisible(true);
        
        
        work(textArea, args);
        
        
    	
    	
    }
    
    public static void work(JTextArea textArea, String[] args) {
    	RelizcLogger.info("Enigma: 1.0.3 Not Equipped with Anticheat");
    	RelizcLogger.info("Runtime arguments: [" + String.join(", ", args) + "]");
    	
    	RelizcLogger.info("NettyService server " + Client.host + ":" + Client.port);
    	
    	String versionCheck = Client.offer("/version");
    	if (versionCheck == null || versionCheck.length() == 0) {
    		
    		RelizcLogger.info("Failed to check mods. Either NettyService Servers or Mod provider servers might be down");
    		
    		return;
    	}
    	System.out.println(versionCheck);
    	
    	System.out.println("Downloading modlist");
    	
    	String modlist = Client.offer("/modlist");
    	String[] inst = modlist.split("\n");
    	
    	Set<String> all = new HashSet<String>();
    	
    	Map<String, String> hashToURL = new HashMap<String, String>();
    	
    	for (String s : inst) {
    		String[] cont = s.split(" ");
    		String url = cont[cont.length - 1];
    		String hash = cont[cont.length - 2];
    		all.add(hash);
    		hashToURL.put(hash, url);
    	}
    	
    	//String directory = "%appdata%\\.minecraft\\mods";
    	
    	File f = new File(".");
    	String dir = f.getAbsolutePath();
    	
    	List<String> hashed = JarHasher.main(new String[] {dir});
    	
    	if (hashed == null) {
    		return;
    	}
    	
    	for (String hash : hashed) {
    		all.remove(hash);
    		
    		
    	}
    	
    	System.out.println("Missing " + all.size() + " mods");
    	
    	for (String hash : all) {
    		textArea.setCaretPosition(textArea.getDocument().getLength());
    		System.out.println("Missing mod " + hash);
    		String url = hashToURL.getOrDefault(hash, null);
    		if (url == null) {
    			System.out.println("Unable to download " + hash);
    		}
    		System.out.println("Downloading " + url);
    		
    		try {
				Downloader.downloadFile(url, dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    		
    	}
    	
    	System.out.println("Completed. Downloaded " + all.size() + " more mods, initial " + hashed.size() + " mods");
    	System.out.println("Feel free to exit");
    }
}
