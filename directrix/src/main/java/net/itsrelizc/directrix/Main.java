package net.itsrelizc.directrix;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import net.itsrelizc.directrix.window.Panel;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	@Override
    public void onEnable() {
        // You should not put an enable message in your plugin.
        // BungeeCord already does so
		
		
		
        getLogger().info("Yay! It loads!");
        
        Panel.main(null);   
	}
	
	public static void main(String[] args) {
		try {
            // Set the look and feel to the current Windows theme
            UIManager.setLookAndFeel("com.apple.laf.AquaLookAndFeel");

            // Alternatively, to force the classic Windows theme, use:
            // UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
		Panel.main(args);
		//JOptionPane.showMessageDialog(null, "Please launch directrix by dragging the jarfile into the BungeeCord plugins folder.", "Invalid Run", JOptionPane.ERROR_MESSAGE);
		return;
	}
}
