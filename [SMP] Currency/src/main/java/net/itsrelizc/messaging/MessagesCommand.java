package net.itsrelizc.messaging;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.menus.Menu2;

public class MessagesCommand extends RelizcCommand {
	
	private byte selection = 0; // 0 - general
	
	public MessagesCommand() {
		super("mail");
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] ntargs) {
		
		MessageMenu menu = new MessageMenu(player);
		Menu2 menuW = new Menu2(player, 6, menu);
		
		menuW.open();
		
		return true;
	}
	
	

	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] ntargs, Location location) {
		
		return TabCompleteInfo.presetNothing((Player) sender);
		
	}
	
	
	
}
