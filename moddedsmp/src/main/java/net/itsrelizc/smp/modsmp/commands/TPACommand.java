package net.itsrelizc.smp.modsmp.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TPACommand implements CommandExecutor {
	
	public static Map<Player, Player> tpas = new HashMap<Player, Player>();
	public static Map<Player, Integer> expire = new HashMap<Player, Integer>();
	public static Map<Player, Integer> cd = new HashMap<Player, Integer>();

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) return false;
		
		final Player player = (Player) sender;
		final Player target = Bukkit.getPlayer(args[0]);

		
		if (target.getName().equalsIgnoreCase(player.getName())) {
			StringUtils.systemMessage(player, Locale.get(player, "commands.tpa"), Locale.get(player, "commands.tpa.errorself"));
			return true;
		}
		
		cd.put(player, 300);
		
		if (target == null) {
			StringUtils.systemMessage(player, Locale.get(player, "commands.tpa"), Locale.get(player, "commands.tpa.errornotonline"));
			return true;
		}
		
		if (tpas.keySet().contains(player)) {
			StringUtils.systemMessage(player, Locale.get(player, "commands.tpa"), Locale.get(player, "commands.tpa.alreadysent"));
			return true;
		}
		
		tpas.put(player, target);
		
		Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("RelizcSMP"), new Runnable() {

			@Override
			public void run() {
				if (tpas.containsKey(player)) {
					StringUtils.systemMessage(player, Locale.get(player, "commands.tpa"), Locale.get(player, "commands.tpa.expired"));
					StringUtils.systemMessage(target, Locale.get(target, "commands.tpa"), String.format(Locale.get(target, "commands.tpa.expired.totarget"), player.getDisplayName()));
					tpas.remove(player);
				}
			}
			
		}, 1200L);
		
		StringUtils.systemMessage(player, Locale.get(player, "commands.tpa"), String.format(Locale.get(player, "commands.tpa.send"), target.getDisplayName()));
		
		TextComponent msg1 = new TextComponent(TextComponent.fromLegacyText(Locale.get(target, "commands.tpa.request.arg0")));
		TextComponent msg2 = new TextComponent(TextComponent.fromLegacyText(Locale.get(target, "commands.tpa.request.arg1")));
		
		TextComponent yes = new TextComponent(TextComponent.fromLegacyText(Locale.get(target, "commands.tpa.request.arg2")));
		yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpy " + player.getDisplayName()));
		yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.format(Locale.get(target, "commands.tpa.request.hoverok"), player.getDisplayName()))));
		
		TextComponent no = new TextComponent(TextComponent.fromLegacyText(Locale.get(target, "commands.tpa.request.arg3")));
		no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpn " + player.getDisplayName()));
		no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(String.format(Locale.get(target, "commands.tpa.request.hoverno"), player.getDisplayName()))));
		
		msg1.addExtra(yes);
		msg1.addExtra(msg2);
		msg1.addExtra(no);
		
		StringUtils.systemMessage(target, Locale.get(target, "commands.tpa"), String.format(Locale.get(target, "commands.tpa.request.sendtoyou"), player.getDisplayName()));
		StringUtils.systemMessage(target, Locale.get(target, "commands.tpa"), String.format(Locale.get(target, "commands.tpa.expiretime")));
		StringUtils.systemMessage(target, Locale.get(target, "commands.tpa"), msg1);
		StringUtils.systemMessage(target, Locale.get(target, "commands.tpa"), String.format(Locale.get(target, "commands.tpa.request.alt"), player.getDisplayName(), player.getDisplayName()));
		
		
		
		return true;
	}

}
