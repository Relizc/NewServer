package net.itsrelizc.string;

import java.sql.Timestamp;
import java.util.Random;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatUtils {
	
	public enum Colors {
		DARK_BLUE("1"),
		DARK_GREEN("2"),
		DARK_AQUA("3"),
		DARK_RED("4"),
		DARK_PURPLE("5"),
		GOLD("6"),
		GRAY("7"),
		DARK_GRAY("8"),
		BLUE("9"),
		GREEN("a"),
		AQUA("b"),
		RED("c"),
		MAGENTA("d"),
		YELLOW("e"),
		WHITE("f");
		
		
		Colors(String string) {
			this.v = "§" + string;
		}
		
		@Override
		public String toString() {
			return this.v;
		}

		private String v;
	}
	
	public static String emptyColorString(int length) {
		String b = "";
		for (int i = 0; i < length; i ++) {
			int index = new Random().nextInt(Colors.values().length);
			b += Colors.values()[index];
		}
		return b;
	}
	
	public static void systemMessage(Player player, String message) {
		player.sendMessage("§l§dSYSTEM §r§8> §r" + message);
	}
	
	public static void systemMessage(Player player, String channel, String message) {
		player.sendMessage(channel.toUpperCase() + " §r§8> §r" + message);
	}
	
	public static void systemMessage(Player player, ChatColor color, String channel, String message) {
		player.sendMessage(color + "§l" + channel.toUpperCase() + " §r§8> §r" + message);
	}

	public static void systemMessage(CommandSender sender, String channel, String message) {
		sender.sendMessage(channel.toUpperCase() + " §r§8> §r" + message);
	}
	
	public static String plural(Integer number) {
		if (number > 1) return "s";
		else return "";
	}
	
	/*
	 * Sorts an array by padding it with Minecraft color codes (§0-9a-f)
	 * Used to sort tab complete commands
	 * 
	 * */
	public static List<String> configOrder(List<String> initial) {
		return initial;
		
	}
	
	public static void broadcastSystemMessage(String channel, String message) {
		Bukkit.broadcastMessage(channel.toUpperCase() + " §r§8> §r" + message);
	}
	
	public static void broadcastSystemMessage(String channel, TextComponent content) {
		TextComponent c = new TextComponent(TextComponent.fromLegacyText(channel.toUpperCase() + " §r§8> §r"));
		c.addExtra(content);
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.spigot().sendMessage(c);
		}
	}
	
	public static void npcMessage(Player player, String name, String content) {
		player.sendMessage("§d[NPC] " + name + "§7:§r " + content);
	}
	
	public static void adminCommand(Player player, Command command) {
		player.sendMessage("§7§o[" + player.getDisplayName() + ": ");
	}
	
	public static void attachCommand(TextComponent component, String command, String altHoverText) {
		component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
		if (altHoverText == null) {
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§aClick me to run the following command:\n\n§b/" + command).create()));
		} else {
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a" + altHoverText).create()));
		}
	}
	
	public static void attachHover(TextComponent component, String hover) {
		if (hover == null) {
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eTheres nothing to see here!").create()));
		} else {
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		}
	}
	
	public static void attachOpenURL(TextComponent component, String url) {
		component.setText(component.getText() + " ↗");
		component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
	}

	
	public static List<String> fromGeneticString(String list[]) {
		List<String> a = new ArrayList<String>();
		for (int i = 0; i < list.length; i ++) {
			a.add(list[i]);
		}
		return a;
	}
	
	public static List<String> reversedFromGeneticString(String list[]) {
		List<String> a = new ArrayList<String>();
		for (int i = list.length - 1; i >= 0; i --) {
			a.add(list[i]);
		}
		return a;
	}
	
	public static List<String> fromArgs(String... list) {
		return fromGeneticString(list);
	}
	
	public static List<String> reversedFromArgs(String... list) {
		return reversedFromGeneticString(list);
	}
	
	public static List<String> fromNewList() {
		return new ArrayList<String>();
	}
	
	public static List<String> fromObjects(Object[] objs) {
		List<String> a = new ArrayList<String>();
		for (Object obj : objs) {
			a.add(objs.toString().toLowerCase());
		}
		return a;
	}

	public static void systemMessage(Player player, String channel, TextComponent content) {
		TextComponent c = new TextComponent(TextComponent.fromLegacyText(channel.toUpperCase() + " §r§8> §r"));
		c.addExtra(content);
		player.spigot().sendMessage(c);
	}
}
