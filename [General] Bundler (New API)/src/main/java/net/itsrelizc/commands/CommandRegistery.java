package net.itsrelizc.commands;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;

public class CommandRegistery {
	
	public static void register(RelizcCommand command) {
		
		try {
			   final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

			   bukkitCommandMap.setAccessible(true);
			   CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

			   commandMap.register("relizc", command);
			} catch(Exception e) {
			   e.printStackTrace();
			}
		
	}

}
