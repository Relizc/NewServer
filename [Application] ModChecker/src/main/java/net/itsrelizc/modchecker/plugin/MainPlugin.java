package net.itsrelizc.modchecker.plugin;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.modchecker.client.TerminalClient;
import net.itsrelizc.modchecker.plugin.events.ListenerPlayerJoinLeave;
import net.itsrelizc.modchecker.server.TerminalServer;

public class MainPlugin extends JavaPlugin {
	
	public static TerminalClient client;
	public static JavaPlugin plugin;
	
	@Override
	public void onEnable() {
		System.out.println("Enigma: Attempting to connect to anticheat server");
		Thread run = new Thread() {

			@Override
			public void run() {
//				TerminalServer.main(new String[] {"-server", "-host", "127.0.0.1", "-port", "765", "-modsDirectory", "C:\\Users\\relizc\\Desktop\\mohist\\mods"});
				
				 client = new TerminalClient("10.1.20.7", 765, true);
				 TerminalClient.client = client;
				
			}
			
		};
		
		run.start();
		
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerJoinLeave(), this);
		plugin = this;
	}

}
