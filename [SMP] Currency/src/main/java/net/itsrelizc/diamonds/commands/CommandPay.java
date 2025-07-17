package net.itsrelizc.diamonds.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.commands.RelizcCommand.TabCompleteInfo;
import net.itsrelizc.commands.RelizcCommand.TabCompleteType;
import net.itsrelizc.diamonds.DiamondJar;
import net.itsrelizc.diamonds.DiamondPurse;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.menus.Menu2;
import net.itsrelizc.menus.MenuTemplate2;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.corps.Contract;
import net.itsrelizc.smp.corps.Contract.Agreement;
import net.itsrelizc.smp.corps.Contract.Expire;
import net.itsrelizc.smp.corps.Contract.Party;
import net.itsrelizc.smp.corps.Contract.Expire.ExpireType;
import net.itsrelizc.string.MathEvaluator;
import net.itsrelizc.string.StringUtils;

public class CommandPay extends RelizcCommand {
	
	
	public CommandPay() {
		super("pay", "lets u to pay someone");
		this.setRelizcOp(false);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] ntargs) {
		
		
		
		if (ntargs.length >= 2) {
			
			Player who = Bukkit.getPlayer(ntargs[0]);
			
			String[] args = {ntargs[1], ntargs[0]};

			Long ct = null;
			long bal = DiamondPurse.getPurse(player);
			
			if (args[0].equalsIgnoreCase("max")) {
				
				ct = bal;
				
			} else if (args[0].endsWith("%") && isNumeric(args[0].substring(0, args[0].length() - 1))) {
				
				
				
				double ratio = 0;
				
				String kf = args[0].replaceAll("%", "%%");
				
				try {
					ratio = Double.valueOf(args[0].substring(0, args[0].length() - 1));
				} catch (Exception e) {
					player.playSound(player, Sound.ENTITY_SHULKER_AMBIENT, 1f, 0f);
					StringUtils.systemMessage(player, Locale.get(player, "commands.pay"), Locale.get(player, "commands.pay.fail.format"));
					return true;
				}
				ratio /= 100.0;
				ct = (long) (bal * ratio);
				
			} else if (args[0].equalsIgnoreCase("half")) {
				
				ct = bal / 2;
				
			} else if (args[0].equalsIgnoreCase("quarter")) {
				ct = bal / 4;

			} else if (args[0].equalsIgnoreCase("eighth")) {
				ct = bal / 8;

			} else if (args[0].length() > 0) {
				
				String formula = args[0];
				formula = formula.toLowerCase();
				formula = formula.replaceAll("purse", "" + bal);
				
				ct = MathEvaluator.evaluate(formula);
				
				if (ct == null) {
					player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 1f, 0f);
					StringUtils.systemMessage(player, Locale.get(player, "commands.pay"), Locale.get(player, "commands.pay.fail.format"));
					return true;
				}
				
				
			}	
			
			if (bal - ct < 0) {
				player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 2f);
				StringUtils.systemMessage(player, Locale.get(player, "commands.pay"), Locale.get(player, "commands.pay.fail.notenough"));
				return true;
			} 
			
			if (player.getUniqueId().equals(who.getUniqueId())) {
				player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 2f);
				StringUtils.systemMessage(player, Locale.get(player, "commands.pay"), Locale.get(player, "commands.pay.fail.sameguy"));
				return true;
			}
			
			DiamondPurse.removePurse(player, ct);
			player.playSound(player, Sound.ENTITY_VILLAGER_YES, 1f, 2f);
			StringUtils.systemMessage(player, Locale.get(player, "commands.pay"), Locale.get(player, "commands.pay.sucess").formatted(Profile.coloredName(who), ct));
			
			DiamondPurse.addPurse(who, (long) ct);
			who.playSound(who, Sound.ENTITY_VILLAGER_YES, 1f, 2f);
			StringUtils.systemMessage(who, Locale.get(who, "commands.pay"), Locale.get(who, "commands.pay.sucess.from").formatted(Profile.coloredName(player), ct));
			
		} else {
			player.playSound(player, Sound.ENTITY_VILLAGER_NO, 1f, 2f);
			StringUtils.systemMessage(player, Locale.get(player, "commands.pay"), Locale.get(player, "commands.pay.fail.notarget"));
			return true;
			
		}
		
		return true;
		
	}
	
	private static boolean isNumeric(String s) {
		try {
			Double.valueOf(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@Override
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] ntargs, Location location) {
		
		if (ntargs.length == 1) {
			return TabCompleteInfo.presetPlayer((Player) sender, "commands.pay.arg1");
		}
		
		else if (ntargs.length == 2) {
			
			String[] args = {ntargs[1], ntargs[0]};
			
			Player player = (Player) sender;
			
			String desc;
			long ct;
			long bal = DiamondPurse.getPurse(player);
			
			if (args[0].equalsIgnoreCase("max")) {
				
				ct = bal;
				desc = "(?) MAX (" + Locale.get((Player) sender, "commands.general.max") + ") = %,d ct";
				
			} else if (args[0].endsWith("%") && isNumeric(args[0].substring(0, args[0].length() - 1))) {
				
				
				
				double ratio = 0;
				
				String kf = args[0].replaceAll("%", "%%");
				
				try {
					ratio = Double.valueOf(args[0].substring(0, args[0].length() - 1));
				} catch (Exception e) {
					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
							"(?) " + Locale.get((Player) sender, "commands.pay.advanced").formatted(bal),
							"(X) " + Locale.get(player, "commands.pay.ratio.invalid")));
				}
				ratio /= 100.0;
				desc = "(?) " + Locale.get(player, "commands.pay.ofbalance").formatted(kf)  + " = %,d ct (" + Locale.get(player, "commands.pay.percentifasdecimal").formatted(ratio, "(" + kf + ")") +")";
				ct = (long) (bal * ratio);
				
			} else if (args[0].equalsIgnoreCase("half")) {
				
				ct = bal / 2;
				desc = "(?) HALF (" + Locale.get((Player) sender, "commands.general.half") + ") = %,d ct";
				
			} else if (args[0].equalsIgnoreCase("quarter")) {
				ct = bal / 4;
				desc = "(?) QUARTER (" + Locale.get((Player) sender, "commands.general.quarter") + ") = %,d ct";

			} else if (args[0].equalsIgnoreCase("eighth")) {
				ct = bal / 8;
				desc = "(?) EIGHTH (" + Locale.get((Player) sender, "commands.general.eighth") + ") = %,d ct";

			} else if (args[0].length() > 0) {
				
				String formula = args[0];
				formula = formula.toLowerCase();
				formula = formula.replaceAll("purse", "" + bal);
				
				ct = MathEvaluator.evaluate(formula);
				
				if (ct == -1) {
					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.pay.arg0.description"), StringUtils.fromArgs(
							"(?) " + Locale.get((Player) sender, "commands.pay.advanced").formatted(bal),
							"(X) " + Locale.get(player, "commands.pay.invalid")));
				}
				
				desc = "(?) " + args[0].replaceAll("%", "%%") + " = %,d ct";
				
				
			} else {
				return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.pay.arg0.description"), StringUtils.fromArgs(
						"(?) " + Locale.get((Player) sender, "commands.pay.advanced").formatted(bal),
						"MAX",
						"HALF",
						"QUARTER",
						"EIGHTH",
						"33%",
						"66.66%",
						(int) (bal * 10.0) / 100.0 + "k",
						"20+(40%/3^2)/(e+purse)"));
			}
			
			String s = Locale.get(player, "commands.pay.balance").formatted(bal, bal - ct);
			
			if (bal - ct < 0) {
				s += " " + Locale.get(player, "commands.pay.balance.notenough");
			}
			
//			//(desc);
			
			return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.pay.arg0.description"), StringUtils.fromArgs(
					desc.formatted(ct),
					"(?) " + s));
			
		}
		
		return TabCompleteInfo.presetNothing((Player) sender);
		
	}
	
	
	
}
