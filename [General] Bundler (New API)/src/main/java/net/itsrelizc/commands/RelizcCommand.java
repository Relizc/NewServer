package net.itsrelizc.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

public class RelizcCommand extends BukkitCommand {
	
	public static enum TabCompleteType {
		
		NUMBER("commands.general.number"),
		TEXT("commands.general.text"),
		PLAYER("commands.general.player"),
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
		private List<String> preset = null;
		private boolean optional = false;
		private boolean force = false;
		
		TabCompleteInfo(TabCompleteType[] type) {
			tab = type;
		}
		
		public TabCompleteInfo(TabCompleteType[] type, Player player, String description) {
			tab = type;
			this.player = player;
			this.description = description;
		}
		
		public TabCompleteInfo(TabCompleteType[] type, Player player, String description, List<String> preset) {
			tab = type;
			this.player = player;
			this.description = description;
			this.preset = preset;
		}
		
		public TabCompleteInfo(boolean optional, TabCompleteType[] tabCompleteTypes, Player sender, String string) {
			tab = tabCompleteTypes;
			this.player = sender;
			this.description = string;
			this.optional  = optional;
		}

		public TabCompleteInfo(boolean forceShow, TabCompleteType[] tabCompleteTypes, Player sender, String string,
				List<String> fromArgs) {
			tab = tabCompleteTypes;
			this.player = sender;
			this.description = string;
			this.preset = fromArgs;
			this.force  = forceShow;
		}

		public List<String> raw(String[] args) {
			
			String already = null;
			if (args.length >= 1) {
				already = args[args.length - 1];
			}
			
			if (player == null) {
				return StringUtils.reversedFromArgs("TabComplete not supported for console");
			}
			
			if (tab.length == 1) {
				
				String add = "";
				if (optional) {
					add = "(" + Locale.get(player, "commands.general.optional") + ") ";
				}
				
				List<String> a = StringUtils.fromArgs(
						" " + add + Locale.get(player, "commands.general.tabcomplete.argdesc").formatted(Locale.get(player, tab[0].getName()), Locale.get(player, description)) // [输入一个%s: %s]
						);
				
				if (this.preset != null) {
					
					if (already == null || already.trim().length() == 0 || force) {
						return Stream.concat(a.stream(), preset.stream()).toList();
					} else {
						List<String> suggests = StringUtils.fromNewList();
						for (String s : preset) {
							if (s.startsWith(already.toUpperCase()) || s.startsWith(already.toLowerCase())) suggests.add(s);
						}
						if (suggests.size() == 0) {
							a.add(" [%s]".formatted(Locale.a(player, "commands.general.tabcomplete.nosuggestions")));
							return a;
						} else return suggests;
						
					}
					
					
				} else {
					return a;
				}
			} else if (tab.length == 0) {
				
				return StringUtils.fromArgs();
				
			} else {
				return StringUtils.fromArgs("some");
			}
			
			
		}

		public static TabCompleteInfo presetPlayer(Player player, String description) {
			List<String> s = StringUtils.fromNewList();
			for (Player p : Bukkit.getOnlinePlayers()) {
				s.add(p.getName());
			}
			return new TabCompleteInfo(new TabCompleteType[] {TabCompleteType.PLAYER}, player, Locale.get(player, description), s);
		}

		public static TabCompleteInfo presetNothing(Player player) {
			return new TabCompleteInfo(new TabCompleteType[] {}, player, null);
		}

		public static TabCompleteInfo presetOption(Player sender, String string, List<String> names) {
			return new TabCompleteInfo(new TabCompleteType[] {TabCompleteType.OPTION}, sender, Locale.get(sender, string), names);
		}

		public static TabCompleteInfo presetNumber(Player sender, String string) {
			return new TabCompleteInfo(new TabCompleteType[] {TabCompleteType.NUMBER}, sender, Locale.get(sender, string));
		}

		public static TabCompleteInfo presetNumber(boolean optional, Player sender, String string) {
			return new TabCompleteInfo(optional, new TabCompleteType[] {TabCompleteType.NUMBER}, sender, Locale.get(sender, string));
		}
		
		public static TabCompleteInfo presetNumberDelocalized(boolean optional, Player sender, String string) {
			return new TabCompleteInfo(optional, new TabCompleteType[] {TabCompleteType.NUMBER}, sender, string);
		}
		


		public static TabCompleteInfo presetOfflinePlayer(Player sender, String string) {
			List<String> s = StringUtils.fromNewList();
			for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
				s.add(p.getName());
			}
			return new TabCompleteInfo(new TabCompleteType[] {TabCompleteType.PLAYER}, sender, Locale.get(sender, string), s);
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

	
	public void setRelizcLevel(long value) {
		this.relizcpermission = value;
	}
	
	public void setRelizcOp(boolean mustOpRun) {
		this.relizcop = mustOpRun;
		if (mustOpRun) {
			this.setPermission("main.admin");
		} else {
			this.setPermission("main.player");
		}
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
		
		StringUtils.systemMessage(sender, "§c§lERROR", "This command is not supported for Players nor Console! Feel free to reach out to server moderators if you have any concerns.");
		
		return true;
	}
	
	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) {
		return onTabComplete(sender, alias, args, location).raw(args);
		
	}
	
	public TabCompleteInfo onTabComplete(CommandSender sender, String alias, String[] args, Location location) {
		//return new TabCompleteInfo(new TabCompleteType[] {}, (Player) sender, "commands.general.tabcomplete.test");
		return new TabCompleteInfo(new TabCompleteType[] {}, (Player) sender, "commands.general.tabcomplete.test");
		
	}
	
	public void onError(CommandSender sender, Exception error) {
		error.printStackTrace();
		StringUtils.systemMessage(sender, "§c§lERROR", "There was an error while running this command. Please contact the server moderators about this!");
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
			
			StringUtils.attachHover(b, Locale.get(sender, "commands.general.error_debug_stack") + "\n\n§7" + sStackTrace);
			a.addExtra(b);
		}
		
		StringUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), a);
	} 
	
	public void onNoPermission(Player sender) {
		StringUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), Locale.get(sender, "commands.general.error_nopermission"));
	}
	
	public boolean onConsoleExecute(ConsoleCommandSender sender, String[] args) {
		
		StringUtils.systemMessage(sender, "§c§lERROR", "This command cannot be run by Console.");
		
		return true;
		
	}
	
	public boolean onPlayerExecute(Player sender, String[] args) {
		
		StringUtils.systemMessage(sender, Locale.get(sender, "commands.general.error"), Locale.get(sender, "commands.general.error_noplayer"));
		
		return true;
		
	}
	
	@Override
	public String toString() {
		return "RelizcCommand{name=%s,relizcOp=%s,relizcPermission=%s}".formatted(this.getName(),this.relizcop,this.relizcpermission);
	}
}
