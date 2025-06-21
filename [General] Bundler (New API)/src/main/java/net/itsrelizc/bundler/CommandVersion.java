package net.itsrelizc.bundler;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class CommandVersion extends RelizcCommand {

	public CommandVersion() {
		super("version", "checkstheversion");
		// TODO Auto-generated constructor stub
		this.setAliases(StringUtils.fromArgs("ver"));
	}

	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		player.sendMessage(Locale.a(player, "general.version.info").formatted(Bukkit.getServer().getVersion(), Main.getVersion(), Bukkit.getBukkitVersion()));
		return true;
	}
}
