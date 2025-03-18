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
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.corps.Contract;
import net.itsrelizc.smp.corps.Contract.Agreement;
import net.itsrelizc.smp.corps.Contract.Expire;
import net.itsrelizc.smp.corps.Contract.Party;
import net.itsrelizc.smp.corps.Contract.Expire.ExpireType;
import net.itsrelizc.string.MathEvaluator;
import net.itsrelizc.string.StringUtils;

public class CommandSpit extends RelizcCommand {
	
	private static class MenuSpit extends MenuTemplate2 {
		
		private ItemStack title() {
			
			List<String> sep = StringUtils.wrap(Locale.get(getPlayer(), "menu.spit.jar.lore"), 16);
			List<String> alt = StringUtils.fromNewList();
			for (String s : sep) {
				alt.add("§7" + s);
			}
			
			return ItemGenerator.generateByList(DiamondJar.createFor(getPlayer(), 0), 1, 
					Locale.get(getPlayer(), "menu.spit.jar.title"),
					alt);
			
		}
		
		private ItemStack selectValue() {
			

			
			return ItemGenerator.interactiveMenuItem(Material.OAK_SIGN, "§e" + Locale.get(getPlayer(), "menu.spit.select.name"), 	
					StringUtils.fromArgs("§7" + Locale.get(getPlayer(), "menu.spit.select.lore")), "§b" + Locale.get(getPlayer(), "menu.click").formatted(Locale.get(getPlayer(), "menu.spit.select.click")));
		}
		
		private ItemStack parseMax(ItemStack original) {
			
			List<String> lore = original.getItemMeta().getLore();
			lore.add("");
			lore.add("§7" + Locale.get(getPlayer(), "menu.spit.maximum").formatted(DiamondPurse.getMaximumBrew(getPlayer())));
			
			original.getItemMeta().setLore(lore);
			
			return original;
			
		}

		public MenuSpit(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}
		
		
		@Override
		public void apply() {
			this.defaultPreset();
			
			this.setItem(4, title());
			this.setItem(22, parseMax(selectValue()));
		}
		
		@Override
		public void onClick(InventoryClickEvent event) {
			
			if (event.getSlot() == 19) {

				
				
			}
			
		}
		
	}
	
	public CommandSpit() {
		super("spit", "spits diamond to diamondbottle");
		this.setRelizcOp(false);
		
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean onPlayerExecute(Player player, String[] args) {
		
		if (args.length >= 1) {

			Double ct = null;
			double bal = DiamondPurse.getPurse(player);
			
			if (args[0].equalsIgnoreCase("max")) {
				
				ct = bal;
				
			} else if (args[0].endsWith("%") && isNumeric(args[0].substring(0, args[0].length() - 1))) {
				
				
				
				double ratio = 0;
				
				String kf = args[0].replaceAll("%", "%%");
				
				try {
					ratio = Double.valueOf(args[0].substring(0, args[0].length() - 1));
				} catch (Exception e) {
					player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 1f, 0f);
					StringUtils.systemMessage(player, Locale.get(player, "commands.spit"), Locale.get(player, "commands.spit.fail.format"));
					return true;
				}
				ratio /= 100.0;
				ct = bal * ratio;
				
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
					StringUtils.systemMessage(player, Locale.get(player, "commands.spit"), Locale.get(player, "commands.spit.fail.format"));
					return true;
				}
				
				
			}
			
			ct = (Math.round(ct * 1000.0)) / 1000.0;
			
			if (bal - ct < 0) {
				player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 1f, 0f);
				StringUtils.systemMessage(player, Locale.get(player, "commands.spit"), Locale.get(player, "commands.spit.fail.notenough"));
				return true;
			}
			
			DiamondPurse.removePurse(player, ct);
			DiamondJar.createFor(player, (long) (ct * 1000));
			player.playSound(player.getLocation(), Sound.ENTITY_SHULKER_AMBIENT, 1f, 2f);
			StringUtils.systemMessage(player, Locale.get(player, "commands.spit"), Locale.get(player, "commands.spit.sucess").formatted(ct));
			
		} else {
			Menu2 menu = new Menu2(player, 6, new MenuSpit(Locale.get(player, "menu.spit.title")));
			menu.open();
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
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		
		if (args.length == 1) {
			
			Player player = (Player) sender;
			
			String desc;
			Double ct;
			double bal = DiamondPurse.getPurse(player);
			
			if (args[0].equalsIgnoreCase("max")) {
				
				ct = bal;
				desc = "(?) MAX (" + Locale.get((Player) sender, "commands.general.max") + ") = %,.3f ct";
				
			} else if (args[0].endsWith("%") && isNumeric(args[0].substring(0, args[0].length() - 1))) {
				
				
				
				double ratio = 0;
				
				String kf = args[0].replaceAll("%", "%%");
				
				try {
					ratio = Double.valueOf(args[0].substring(0, args[0].length() - 1));
				} catch (Exception e) {
					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
							"(?) " + Locale.get((Player) sender, "commands.spit.advanced").formatted(bal),
							"(X) " + Locale.get(player, "commands.spit.ratio.invalid")));
				}
				ratio /= 100.0;
				desc = "(?) " + Locale.get(player, "commands.spit.ofbalance").formatted(kf)  + " = %,.3f ct (" + Locale.get(player, "commands.spit.percentifasdecimal").formatted(ratio, "(" + kf + ")") +")";
				ct = bal * ratio;
				
			} else if (args[0].equalsIgnoreCase("half")) {
				
				ct = bal / 2;
				desc = "(?) HALF (" + Locale.get((Player) sender, "commands.general.half") + ") = %,.3f ct";
				
			} else if (args[0].equalsIgnoreCase("quarter")) {
				ct = bal / 4;
				desc = "(?) QUARTER (" + Locale.get((Player) sender, "commands.general.quarter") + ") = %,.3f ct";

			} else if (args[0].equalsIgnoreCase("eighth")) {
				ct = bal / 8;
				desc = "(?) EIGHTH (" + Locale.get((Player) sender, "commands.general.eighth") + ") = %,.3f ct";

			} else if (args[0].length() > 0) {
				
				String formula = args[0];
				formula = formula.toLowerCase();
				formula = formula.replaceAll("purse", "" + bal);
				
				ct = MathEvaluator.evaluate(formula);
				
				if (ct == null) {
					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
							"(?) " + Locale.get((Player) sender, "commands.spit.advanced").formatted(bal),
							"(X) " + Locale.get(player, "commands.spit.invalid")));
				}
				
				desc = "(?) " + args[0].replaceAll("%", "%%") + " = %,.3f ct";
				
				
			} else {
				return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
						"(?) " + Locale.get((Player) sender, "commands.spit.advanced").formatted(bal),
						"MAX",
						"HALF",
						"QUARTER",
						"EIGHTH",
						"33%",
						"66.66%",
						(int) (bal * 10.0) / 100.0 + "k",
						"20+(40%/3^2)/(e+purse)"));
			}
			
			ct = ((int) (ct * 1000)) / 1000.0;
			
			String s = Locale.get(player, "commands.spit.balance").formatted(bal, bal - ct);
			
			if (bal - ct < 0) {
				s += " " + Locale.get(player, "commands.spit.balance.notenough");
			}
			
//			Bukkit.broadcastMessage(desc);
			
			return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
					desc.formatted(ct),
					"(?) " + s));
			
		}
		
		return TabCompleteInfo.presetNothing((Player) sender);
		
//		if (args.length == 1) {
//			
//			double dias = 0;
//			
//			try {
//				dias = Double.valueOf(args[0]);
//			} catch (Exception e) {
//				
//				double ct;
//				double bal = DiamondPurse.getPurse((Player) sender);
//				
//				String s;
//				
//				
//				if (args[0].equalsIgnoreCase("max")) {
//					
//					ct = bal;
//					s = Locale.get((Player) sender, "commands.spit.balance").formatted(bal, bal - ct);
//					if (bal - ct < 0) {
//						s += " " + Locale.get((Player) sender, "commands.spit.balance.notenough");
//					}
//					
//					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
//							"(?) MAX (" + Locale.get((Player) sender, "commands.general.max") + ") = %,.3f ct".formatted(ct),
//							"(?) " + s));
//					
//				} else if (args[0].equalsIgnoreCase("half")) {
//					
//					ct = bal / 2;
//					ct = ((int) (ct * 1000)) / 1000.0;
//					s = Locale.get((Player) sender, "commands.spit.balance").formatted(bal, bal - ct);
//					if (bal - ct < 0) {
//						s += " " + Locale.get((Player) sender, "commands.spit.balance.notenough");
//					}
//					
//					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
//							"(?) HALF (" + Locale.get((Player) sender, "commands.general.half") + ") = %,.3f ct".formatted(ct),
//							"(?) " + s));
//					
//				} else if (args[0].equalsIgnoreCase("quarter")) {
//					ct = bal / 4;
//					ct = ((int) (ct * 1000)) / 1000.0;
//					s = Locale.get((Player) sender, "commands.spit.balance").formatted(bal, bal - ct);
//					if (bal - ct < 0) {
//						s += " " + Locale.get((Player) sender, "commands.spit.balance.notenough");
//					}
//					
//					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
//							"(?) QUARTER (" + Locale.get((Player) sender, "commands.general.quarter") + ") = %,.3f ct".formatted(ct),
//							"(?) " + s));
//				} else if (args[0].equalsIgnoreCase("eighth")) {
//					ct = bal / 8;
//					ct = ((int) (ct * 1000)) / 1000.0;
//					s = Locale.get((Player) sender, "commands.spit.balance").formatted(bal, bal - ct);
//					if (bal - ct < 0) {
//						s += " " + Locale.get((Player) sender, "commands.spit.balance.notenough");
//					}
//					
//					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
//							"(?) EIGHTH (" + Locale.get((Player) sender, "commands.general.eighth") + ") = %,.3f ct".formatted(ct),
//							"(?) " + s));
//				} else {
//					return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
//							"(?) " + Locale.get((Player) sender, "commands.spit.advanced"),
//							"MAX",
//							"HALF",
//							"QUARTER",
//							"EIGHTH",
//							"33%",
//							"66.66%",
//							bal + "ct",
//							(int) (bal * 10.0) / 100.0 + "k",
//							(int) (bal * 1000.0) / 1000.0 + "kct"));
//				}
//				
//				
//			}
//			
//			
//			double ct = dias / 10.0 ;
//			
//			double bal = DiamondPurse.getPurse((Player) sender);
//			
//			String s = Locale.get((Player) sender, "commands.spit.balance").formatted(bal, bal - ct);
//			
//			if (bal - ct < 0) {
//				s += " " + Locale.get((Player) sender, "commands.spit.balance.notenough");
//			}
//			
//			return new TabCompleteInfo(true, new TabCompleteType[] {TabCompleteType.NUMBER},(Player) sender, Locale.get((Player)sender, "commands.spit.arg0.description"), StringUtils.fromArgs(
//					"(?) " + dias + " " + Locale.get((Player) sender, "item.diamond") + " = %,.3f ct".formatted(ct),
//					"(?) " + s));
//		} else {
//			
//			
//			return TabCompleteInfo.presetNothing((Player) sender);
//		}
		
	}
	
	
	
}
