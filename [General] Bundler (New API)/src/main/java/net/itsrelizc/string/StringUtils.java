package net.itsrelizc.string;

import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import net.itsrelizc.players.locales.Locale;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class StringUtils {
	
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
	
	public static enum Channels {
		
		
		
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
	
	/**
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
	
	public static void attachCommand(Player player, TextComponent component, String command) {
		attachCommand(component, command, Locale.get(player, "general.text.runcommand") + command);
	}
	
	public static void attachHover(TextComponent component, String hover) {
		if (hover == null) {
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eTheres nothing to see here!").create()));
		} else {
			component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()));
		}
	}
	
	public static void attachOpenURL(TextComponent component, String url) {
		component.setText(component.getText() + " ↗§r");
		component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
	}
	
	public static void attachCopy(TextComponent component, String toCopy) {
		component.setText(component.getText() + " ⎘");
		component.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, toCopy));
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
	
	public static void message(Player player, String channel, String localizedString) {
		
		systemMessage(player, channel, Locale.get(player, localizedString));
		
	}
	
	public static void message(Player player, String channel, String localizedString, Object[] formats) {
		
		systemMessage(player, channel, Locale.get(player, localizedString).formatted(formats));
		
	}
	
	private static final Set<Character> INVALID_LINE_START = Set.of(
	        ',', '.', '!', '?', ':', ';', ')', ']', '}', '\'', '"'
    );
	
	/**
	 * This function wraps a string text by adding newlines. <strong>Take note that user entered new line characters "\n" does not reset the counter</strong>
	 * @param text The Text to wrap
	 * @param maxWidth The maximum width that a line can have
	 * @return
	 */
    public static String wrapText(String text, int maxWidth) {
        if (text == null || maxWidth < 1) return "";

        String[] words = text.split("\\s+");
        StringBuilder wrapped = new StringBuilder();
        StringBuilder line = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            String word = words[i];

            // Check if this word would start a line with a bad punctuation
            while (!line.isEmpty() && word.length() + line.length() + 1 > maxWidth) {
                // If the next word is just punctuation and would start a new line, attach it to the current line
                if (word.length() == 1 && INVALID_LINE_START.contains(word.charAt(0))) {
                    line.append(word);  // glue punctuation
                    i++;  // move to next word
                    if (i < words.length) word = words[i]; else break;
                } else {
                    break;
                }
            }

            if (line.length() + word.length() + 1 > maxWidth) {
                // Check if the word would start with bad punctuation
                if (!line.isEmpty()) {
                    wrapped.append(line.toString().stripTrailing()).append("\n");
                    line = new StringBuilder();
                }
            }

            if (line.length() > 0) {
                line.append(" ");
            }
            line.append(word);
        }

        if (!line.isEmpty()) {
            wrapped.append(line.toString().stripTrailing());
        }

        return wrapped.toString();
    }
    
    private static final Pattern COLOR_CODE_PATTERN = Pattern.compile("§[0-9a-fk-or]");
    
    /**
     * This is similar to the wrapText function, but instead it also contain styling information, such as color and italics. This will account implicit newline characters.
     * @param text The text to wrap
     * @param maxWidth Maximum characters in a line.
     * @return
     */
    public static String wrapTextColor(String text, int maxWidth) {
        if (text == null || maxWidth < 1) return "";

        // Split the text by actual newlines to preserve user-entered line breaks
        String[] lines = text.split("\n");
        StringBuilder wrapped = new StringBuilder();
        String activeColor = "§r";  // Default reset color

        for (String line : lines) {
            StringBuilder wrappedLine = new StringBuilder(activeColor);  // Start with current active color for each line
            StringBuilder currentLine = new StringBuilder(activeColor);
            String[] words = line.split(" ");
            
            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                String cleanWord = stripColorCodes(word);

                // Track new color codes in the word
                Matcher matcher = COLOR_CODE_PATTERN.matcher(word);
                while (matcher.find()) {
                    activeColor = matcher.group();  // last match is the current color
                }

                // Check if the word fits in the current line or if it needs to wrap
                int currentLineLength = stripColorCodes(currentLine.toString()).length();
                boolean wordTooLong = currentLineLength + cleanWord.length() + 1 > maxWidth;

                if (wordTooLong) {
                    // Append the wrapped line and start a new one with the current color
                    wrapped.append(currentLine.toString().stripTrailing()).append("\n");
                    currentLine = new StringBuilder(activeColor);
                } else if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }

                currentLine.append(word);
            }

            // Append the last wrapped line for the current user-entered line
            wrapped.append(currentLine.toString().stripTrailing()).append("\n");
        }

        // Remove the last newline character (if it was added)
        if (wrapped.length() > 0 && wrapped.charAt(wrapped.length() - 1) == '\n') {
            wrapped.deleteCharAt(wrapped.length() - 1);
        }

        return wrapped.toString();
    }

    // Helper to remove color codes for accurate length calculation
    private static String stripColorCodes(String input) {
        return COLOR_CODE_PATTERN.matcher(input).replaceAll("");
    }
    
    public static List<String> wrap(String text, int maxWidth) {
        return StringUtils.fromArgs(wrapText(text, maxWidth).split("\n"));
    }
	
	public static long[] convertMillisToTime(long milliseconds) {
        // Calculate the number of days
        long days = milliseconds / (24 * 60 * 60 * 1000);
        milliseconds %= (24 * 60 * 60 * 1000);
        
        // Calculate the number of hours
        long hours = milliseconds / (60 * 60 * 1000);
        milliseconds %= (60 * 60 * 1000);
        
        // Calculate the number of minutes
        long minutes = milliseconds / (60 * 1000);
        milliseconds %= (60 * 1000);
        
        // Calculate the number of seconds
        long seconds = milliseconds / 1000;
        
        // Return the result as a formatted string
        return new long[] {days, hours, minutes, seconds};
    }
}
