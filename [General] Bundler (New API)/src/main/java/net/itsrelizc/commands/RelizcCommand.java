package net.itsrelizc.commands;

import java.lang.reflect.Field;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.itsrelizc.string.ChatUtils;

public class RelizcCommand extends BukkitCommand {

	public RelizcCommand(String name) {
		super(name);
		
		this.description = "§bA Relizc Network Command";
		this.usageMessage = "§cInvalid Usage! Try doing /" + name;
		
		this.setPermission("main.player");
		
	}
	
	public RelizcCommand(String name, String description, String usage, String permission, List<String> alias) {
		
		super(name, description, usage, alias);
		
	}
	
	public RelizcCommand(String name, String description, List<String> alias) {
		
		super(name);
		
		this.description = description;
		this.usageMessage = "§cInvalid Usage! Try doing /" + name;
		this.setPermission("main.player");
		this.setAliases(alias);
		
	}

	public RelizcCommand(String string, String string2) {
		super(string);
		this.description = string2;
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (sender instanceof ConsoleCommandSender) {
			return this.onConsoleExecute((ConsoleCommandSender) sender, args);
		} else if (sender instanceof Player) {
			return this.onPlayerExecute((Player) sender, args);
		}
		
		ChatUtils.systemMessage(sender, "§c§lERROR", "This command is not supported for Players nor Console! Feel free to reach out to server moderators if you have any concerns.");
		
		return true;
	}
	
	public boolean onConsoleExecute(ConsoleCommandSender sender, String[] args) {
		
		ChatUtils.systemMessage(sender, "§c§lERROR", "This command cannot be run by Console.");
		
		return true;
		
	}
	
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		ChatUtils.systemMessage(sender, "§c§lERROR", "This command cannot be run by Players.");
		
		return true;
		
	}
	
	

}
