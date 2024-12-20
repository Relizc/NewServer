package net.itsrelizc.smp.modsmp.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.smp.modsmp.menus.Shop;

public class ShopCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = (Player) sender;
		
		Shop shop = new Shop(player);
		shop.openShop();
		
		return true;
	}

}
