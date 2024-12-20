package net.itsrelizc.smp.modsmp.commands;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LOLCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
//		Player target;
//		
//		if (args.length >= 1) {
//			target = Bukkit.getPlayer(args[0]);
//		} else {
//			target = (Player) sender;
//		}
//		
//		for (Sound sound : Sound.values()) {
//			target.playSound(target.getLocation(), sound, 10f, 1f);
//		}
//		
//		return false;
		
		Player player = (Player) sender;
		
		sender.sendMessage(player.getItemInHand().getType().getClass().toString());
		
		return true;
	}

}
