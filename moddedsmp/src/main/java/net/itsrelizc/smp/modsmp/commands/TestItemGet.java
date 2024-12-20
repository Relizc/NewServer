package net.itsrelizc.smp.modsmp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.smp.modsmp.items.ItemLibrary;
import net.itsrelizc.string.ChatUtils;

public class TestItemGet implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		Player player = (Player) sender;
		
		if (!player.getName().equalsIgnoreCase("relizc")) {
			player.sendMessage("you cant use this");
			return true;
		}
		
		String block = args[0];
		
		if (block.equalsIgnoreCase("duper")) {
			player.getInventory().addItem(ItemLibrary.item_itemduper());
		}
		
		
		
		return true;
		
	}
	
}
