package net.itsrelizc.smp.modsmp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.ChatUtils;

public class TPAccept implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			return false;
		}
		
		Player player = (Player) sender;
		Player target = Bukkit.getPlayer(args[0]);
		
		if (target == null) {
			ChatUtils.systemMessage(player, Locale.get(player, "commands.tpa"), String.format(Locale.get(player, "commands.tpy.notonline"), args[0]));
			return true;
		}
		if (!TPACommand.tpas.containsKey(target)) {
			ChatUtils.systemMessage(player, Locale.get(player, "commands.tpa"), String.format(Locale.get(player, "commands.tpy.norequest"), target.getDisplayName()));
			return true;
		}
		
		
		target.teleport(player);
		ChatUtils.systemMessage(player, Locale.get(player, "commands.tpa"), String.format(Locale.get(player, "commands.tpy.success"), target.getDisplayName()));
		ChatUtils.systemMessage(target, Locale.get(target, "commands.tpa"), String.format(Locale.get(player, "commands.tpy.agreed"), player.getDisplayName()));
		
		TPACommand.tpas.remove(target);
		
		return true;
	}

}