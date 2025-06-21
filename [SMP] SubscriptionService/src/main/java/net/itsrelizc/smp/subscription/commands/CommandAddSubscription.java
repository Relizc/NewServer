package net.itsrelizc.smp.subscription.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Profile.SubscriptionType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class CommandAddSubscription extends RelizcCommand {

	public CommandAddSubscription() {
		super("subscription", "subscribe");
		this.setRelizcLevel(2);
		// TODO Auto-generated constructor stub
//		this.setRelizcOp(true);
	}
	
	@Override
	public boolean onPlayerExecute(Player sender, String[] args) {
		
//		Profile p =Profile.findByOwner(sender);
//		if (p.permission < 2) {
//			this.onNoPermission(sender);
//			return true;
//		}
		
		String opt = args[0];
		String value = args[2];
		
		if ("increase".equalsIgnoreCase(opt)) {
			
			StringUtils.systemMessage(sender, Locale.get(sender, "subscription.name"), Locale.get(sender, "command.subscription.setonly"));
			
		} else if ("revoke".equalsIgnoreCase(opt)) {
			
			StringUtils.systemMessage(sender, Locale.get(sender, "subscription.name"), Locale.get(sender, "command.subscription.setonly"));
			
		} else if ("set".equalsIgnoreCase(opt)) {
			
			Profile.setSubscription(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), Long.valueOf(value));
			
		} else if ("set_bundle".equalsIgnoreCase(opt)) {
			
			Profile.setSubscription(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), SubscriptionType.valueOf(value));
			
			
		} else if ("increase_bundle".equalsIgnoreCase(opt)) {
			
//			Profile.addSubscription(Bukkit.getOfflinePlayer(args[1]).getUniqueId(), SubscriptionType.valueOf(value));
			StringUtils.systemMessage(sender, Locale.get(sender, "subscription.name"), Locale.get(sender, "command.subscription.setonly"));
			
		} else {
			StringUtils.systemMessage(sender, Locale.get(sender, "subscription.name"), Locale.get(sender, "command.subscription.setonly"));
		}
		return true;
		
		
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		
		if (args.length == 1) {
			return TabCompleteInfo.presetOption((Player) sender, "commands.subscription.arg0.description", StringUtils.fromArgs("increase", "increase_bundle", "revoke", "set", "set_bundle"));
		} else if (args.length == 2) {
			
			
			return TabCompleteInfo.presetOfflinePlayer((Player) sender, "commands.subscription.arg1.description");
		} else if (args.length == 3){
			
			String opt = args[0];
			if ("increase".equalsIgnoreCase(opt)) {
				return TabCompleteInfo.presetNumber((Player) sender, "commands.subscription.increase.arg0");
			} else if ("revoke".equalsIgnoreCase(opt)) {
				return TabCompleteInfo.presetNothing((Player) sender);
			} else if ("set".equalsIgnoreCase(opt)) {
				return TabCompleteInfo.presetNumber((Player) sender, "commands.subscription.grant.arg0");
			} else if ("set_bundle".equalsIgnoreCase(opt)) {
				return TabCompleteInfo.presetOption((Player) sender, "commands.subscription.grant_bundle.arg0", StringUtils.fromArgs("DAY", "WEEK", "MONTH", "SEASON", "YEAR"));
			} else if ("increase_bundle".equalsIgnoreCase(opt)) {
				return TabCompleteInfo.presetOption((Player) sender, "commands.subscription.grant_bundle.arg0", StringUtils.fromArgs("DAY", "WEEK", "MONTH", "SEASON", "YEAR"));
			} else {
				return TabCompleteInfo.presetNothing((Player) sender);
			}
			
			
		} else {
			return TabCompleteInfo.presetNothing((Player) sender);
		}
		
	}
	
	@Override
	public boolean onConsoleExecute(ConsoleCommandSender sender, String[] args) {
		return true;
		
	}

}
