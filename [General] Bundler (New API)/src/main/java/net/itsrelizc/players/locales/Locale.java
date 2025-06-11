package net.itsrelizc.players.locales;

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
		ZH_CN, ZH_CN_MOJANG,
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
				eng.put((String) key, result);
				
				lang += 1;
			}
			
			locales.put(l.toString(), eng);
			
			System.out.println("Loaded " + lang + " localizations of language " + l.toString());
		}
		
		
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

		return locales.get(Profile.findByOwner(player).lang.toString()).getOrDefault(namespace, namespace);

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
		return locales.get(Profile.findByOwner(player).lang.toString() + "_MOJANG").getOrDefault(namespace, namespace);
	}
	
	public static String get(Player player, String namespace, String... formats) {
		return get(player, namespace).formatted(formats);
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
