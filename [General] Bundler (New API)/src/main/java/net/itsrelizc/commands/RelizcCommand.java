package net.itsrelizc.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.ChatUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class RelizcCommand extends BukkitCommand {
	
	public static enum TabCompleteType {
		
		NUMBER("commands.general.number"),
		TEXT("commands.general.text"),
		OPTION("commands.general.option");

		private String name;

		TabCompleteType(String string) {
			this.name = string;
		}
		
		public String getName() {
			return name;
		}
		
	}
	
	public static class TabCompleteInfo {
		
		private TabCompleteType[] tab;
		private Player player;
		private String description;
		
		TabCompleteInfo(TabCompleteType[] type) {
			tab = type;
		}
		
		TabCompleteInfo(TabCompleteType[] type, Player player, String description) {
			tab = type;
			this.player = player;
			this.description = description;
		}
		
		public List<String> raw() {
			
			if (player == null) {
				return ChatUtils.reversedFromArgs("TabComplete not supported for console");
			}
			
			if (tab.length == 1) {
				
				for (String a : ChatUtils.fromArgs("1", "2", "3")) {
					Bukkit.broadcastMessage(a);
				}	
				
				for (String a : ChatUtils.reversedFromArgs("1", "2", "3")) {
					Bukkit.broadcastMessage(a);
				}
				
				return ChatUtils.fromArgs(
						" " + Locale.get(player, "commands.general.tabcomplete.argdesc").formatted(Locale.get(player, tab[0].getName()), Locale.get(player, description)), // [输入一个%s: %s]
						" " + Locale.get(player, "commands.general.tabcomplete.example").formatted(Locale.get(player, tab[0].getName()), Locale.get(player, tab[0].getName() + ".example")),
						""
						);
			} else if (tab.length == 0) {
				
				return ChatUtils.fromArgs(
						Locale.get(player, "commands.general.tabcomplete.noargs")
						);
				
			} else {
				return ChatUtils.fromArgs("some");
			}
			
			
		}
		
	}

	private long relizcpermission = Long.MIN_VALUE;
	private boolean relizcop;

	public RelizcCommand(String name) {
		super(name);
		
		this.description = "§bA Relizc Network Command";
		this.usageMessage = "§cInvalid Usage! Try doing /" + name;
		
		this.setPermission("main.player");
		
	}
	
	public void setRelizcPermission(long value) {
		this.relizcpermission = value;
	}
	
	public void setRelizcOp(boolean mustOpRun) {
		this.relizcop = mustOpRun;
	}
	
	public RelizcCommand(String name, String description, String usage, String permission, List<String> alias) {
		
		super(name, description, usage, alias);
		this.setPermission("main.player");
		
	}
	
	public RelizcCommand(String name, String description, List<String> alias) {
		
		super(name);
		
		this.description = description;
		this.usageMessage = "§cInvalid Usage! Try doing /" + name;
		this.setPermission("main.player");
		this.setAliases(alias);
		
	}

	public RelizcCommand(String string, String desc) {
		super(string);
		this.description = desc;
	}

	@Override
	public boolean execute(CommandSender sender, String alias, String[] args) {
		
		if (sender instanceof ConsoleCommandSender) {
			try {
				return this.onConsoleExecute((ConsoleCommandSender) sender, args);
			} catch (Exception e) {
				this.onError((ConsoleCommandSender) sender, e);
			}
		} else if (sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if (Profile.findByOwner(p).level < this.relizcpermission) {
				System.out.println("No level");
				this.onNoPermission(p);
				return true;
			}
			
			if ((p.isOp() ? 1 : 0) < (this.relizcop ? 1 : 0)) {
				System.out.println("No op");
				this.onNoPermission(p);
				return true;
			}
			
			try {
				return this.onPlayerExecute((Player) sender, args);
			} catch (Exception e) {
				this.onError((Player) sender, e);
				return true;
			}
			
		}
		
		ChatUtils.systemMessage(sender, "§c§lERROR", "This command is not supported for Players nor Console! Feel free to reach out to server moderators if you have any concerns.");
		
		return true;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) {
		return onTabComplete(sender, alias, args, location).raw();
		
	}
	
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		return new TabCompleteInfo(new TabCompleteType[] {TabCompleteType.NUMBER}, (Player) sender, "commands.general.tabcomplete.test");
		
	}
	
	public void onError(CommandSender sender, Exception error) {
		error.printStackTrace();
		ChatUtils.systemMessage(sender, "§c§lERROR", "There was an error while running this command. Please contact the server moderators about this!");
	} 
	
	public void onError(Player sender, Exception error) {
		error.printStackTrace();
		
		TextComponent a = new TextComponent(Locale.get(sender, "commands.general.error_description"));
		TextComponent b = new TextComponent();
		
		
		if (true) {
			a.addExtra(" ");
			b.setText("§8[?]");
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			error.printStackTrace(pw);
			String sStackTrace = sw.toString().replace("\t", "    ");
			
			ChatUtils.attachHover(b, Locale.get(sender, "commands.general.error_debug_stack") + "\n\n§7" + sStackTrace);
			a.addExtra(b);
		}
		
		ChatUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), a);
	} 
	
	public void onNoPermission(Player sender) {
		ChatUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), Locale.get(sender, "commands.general.error_nopermission"));
	}
	
	public boolean onConsoleExecute(ConsoleCommandSender sender, String[] args) {
		
		ChatUtils.systemMessage(sender, "§c§lERROR", "This command cannot be run by Console.");
		
		return true;
		
	}
	
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		ChatUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), Locale.get(sender, "commands.general.error_noplayer"));
		
		return true;
		
	}
	
	

}
