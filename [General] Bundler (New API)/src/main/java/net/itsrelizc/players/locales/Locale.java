package net.itsrelizc.players.locales;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.Profile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

/**
 * General locale support for multilingual players.
 */
public class Locale {
	
	/**
	 * Enum language for all available languages.
	 */
	public static enum Language {
		EN_US(45),
		ZH_CN(15), ZH_CN_MOJANG,
		ZH_TW, JA_JP, KO_KR, ES_ES;

		private int wrapLength;

		Language(int i) {
			wrapLength = i;
		}

		Language() {
			wrapLength = 15;
		}
		
		/**
		 * Mainly for Minecraft chat, scoreboards, or item lore usages. Since
		 * differnt languages have different character length, some languages 
		 * takes more space even with the same amount of characters. This gets the 
		 * default wrap length for a language. For instance, English supports 45 characters
		 * per line, while Chinese and Asian Hierloglyphics only supports 15. 
		 * @return The recommended character amount per line.
		 */
		public int getStandardItemWrapLength() {
			return wrapLength;
			
		}
		
	}
	
	/*
	 * Converts a long epoch millisecond to the language's human readable format.
	 */
	public static String convertDate(long epochMillis, Language lang) {
        // Convert epoch to Instant
        Instant instant = Instant.ofEpochMilli(epochMillis);

        // Map your Language enum to Java Locale
        java.util.Locale locale = getLocale(lang);

        // Create a formatter with locale
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withLocale(locale)
                .withZone(ZoneId.systemDefault());

        return formatter.format(instant);
    }

    private static java.util.Locale getLocale(Language lang) {
        switch (lang) {
            case EN_US:
                return java.util.Locale.US;
            case ZH_CN:
            case ZH_CN_MOJANG:
                return java.util.Locale.SIMPLIFIED_CHINESE;
            case ZH_TW:
                return java.util.Locale.TRADITIONAL_CHINESE;
            case JA_JP:
                return java.util.Locale.JAPAN;
            case KO_KR:
                return java.util.Locale.KOREA;
            case ES_ES:
                return new java.util.Locale("es", "ES");
            default:
                return java.util.Locale.getDefault();
        }
    }
	
	public static HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String, String>>();
	
	/**
	 * loads all locales
	 */
	public static void load_all() {
		
		for (Language l : Language.values()) {
			JSONObject a1 = JSON.loadDataFromDataBase("lang\\" + l.toString().toLowerCase() + ".json");

			HashMap<String, String> eng = new HashMap<String, String>();
			int lang = 0;
			for (Object key : a1.keySet()) {
				String result = (String) a1.get(key);
				eng.put(((String) key).toLowerCase(), result);
				
				lang += 1;
			}
			
			locales.put(l.toString(), eng);
			
			System.out.println("Loaded " + lang + " localizations of language " + l.toString());
		}
		
		
	}
	
	public static void addEntry(String lang, String key, String value) {
		locales.get(lang).put(key, value);
	}
	
	/**
	 * Gets the declared language entries in this language's JSON file. Mainly used for calculating language
	 * completion rate and debugging.
	 * @param code The language
	 * @return the amount of entries this language have.
	 */
	public static int getLinesAmount(Language code) {
		return locales.get(code.toString()).size();
	}
	
	/**
	 * Gets the locale of this player based on their language.
	 * @param player The audience player
	 * @param namespace The translation key, such as {@code commands.jerkoff.deny}
	 * @return The translated string, or the translation key itself if the entry is not present in the language JSON file.
	 */
	public static String get(Player player, String namespace) {
		
		
		
		if (player == null) return get(Language.ZH_CN, namespace.toLowerCase());
		if (namespace == null) return namespace;
		Language lang;
		if (Profile.findByOwner(player) == null) {
			lang = Language.ZH_CN;
		} else {
			lang = Profile.findByOwner(player).lang;
		}

		return locales.get(lang.toString()).getOrDefault(namespace.toLowerCase(), namespace);

	}
	
	/**
	 * Similar to {@link #get(Player, String)}, but instead it gets from the official Mojang language mappings.
	 * Mainly used to get the names of minecraft items. 
	 * In other words, this just gets from the file with {@code "_MOJANG"} attached:
	 * <blockquote>
	 * {@code get(Language.ZH_CN_MOJANG, namespace)}
	 * </blockquote>
	 * @param player The target player audience
	 * @param namespace The Mojang language translation key, such as {@code death.fell.accident.water}
	 * @return The translated string, or the translation key itself if the entry is not present in the language JSON file.
	 */
	public static String getMojang(Player player, String namespace) {
		if (player == null) return getMojang(Language.ZH_CN, namespace.toLowerCase());
		if (player == null || namespace == null) return namespace;
		return locales.get(Profile.findByOwner(player).lang.toString() + "_MOJANG").getOrDefault(namespace, namespace);
	}
	
	public static String getMojang(Language lang, String namespace) {
		return locales.get(lang.toString() + "_MOJANG").getOrDefault(namespace, namespace);
	}
	
	public static String get(Player player, String namespace, Object... formats) {
		return get(player, namespace).formatted(formats);
	}
	
	public static String a(Player player, String namespace) {
		return get(player, namespace);
	}
	
	public static Language getLanguage(Player player) {
		return Profile.findByOwner(player).lang;
	}
	
	public static ItemStack insertSmartLocale(ItemStack it, String localizedName, String... localizedLore) {
		
		CompoundTag tag = NBT.getNBT(it);
		if (tag == null) tag = new CompoundTag();
		
		CompoundTag languages = new CompoundTag();
		NBT.setString(languages, "name", localizedName);
		
		ListTag list = new ListTag();
		for (String s : localizedLore) {
			NBT.addItem(list, s);
		}
		
		NBT.setCompound(languages, "lore", list);
		
		NBT.setCompound(tag, "smartlore", languages);
		return NBT.setCompound(it, tag);
		
	}
	
	public static String get(Language code, String namespace) {
		return locales.get(code.toString()).getOrDefault(namespace, namespace);
	}
	
	public static void sendLanguageInfo(Player player) {
		player.sendMessage("§a您的默认语言以设置为 中文 (简体)。输入 /lang 以切换语言");
		player.sendMessage("§a您的預設語言以設定為 中文 (簡體)。輸入 /lang 以切換語言");
		player.sendMessage("§aYour default language has been set to Chinese (Simplified). Type in /lang to switch languages.");
		player.sendMessage("§a기본 언어는 중국어(간체)로 설정되어 있습니다. 언어를 전환하려면 /lang을 입력하세요.");
		player.sendMessage("§aデフォルトの言語は中国語（簡体字）に設定されています。言語を切り替えるには/langと入力してください");
	}
	
	public static void changeLanguage(Player player, Language lang) {
		
		JSONObject save = JSON.loadDataFromDataBase("players.json");
		JSONObject s2 = (JSONObject) save.get(player.getUniqueId().toString());
		s2.put("lang", lang.toString());
		save.put(player.getUniqueId().toString(), s2);
		JSON.saveDataFromDataBase("players.json", save);
		
	}

}
