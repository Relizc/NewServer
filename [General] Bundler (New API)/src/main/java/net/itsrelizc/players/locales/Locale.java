package net.itsrelizc.players.locales;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.players.Profile;

public class Locale {
	
	public static enum Language {
		EN_US,
		ZH_CN,
		ZH_TW, JA_JP
	}
	
	public static HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String, String>>();
	
	public static void load_all() {
		
		for (Language l : Language.values()) {
			JSONObject a1 = JSON.loadDataFromDataBase("lang\\" + l.toString() + ".json");
			HashMap<String, String> eng = new HashMap<String, String>();
			for (Object key : a1.keySet()) {
				String result = (String) a1.get(key);
				eng.put((String) key, result);
				
				System.out.println("Loading " + l + ": " + key + " " + result);
			}
			
			locales.put(l.toString(), eng);
		}
		
		
	}
	
	public static int getLinesAmount(Language code) {
		return locales.get(code.toString()).size();
	}
	
	public static String get(Player player, String namespace) {
		try {
			return locales.get(Profile.findByOwner(player).lang.toString()).getOrDefault(namespace, namespace);
		} catch (Exception excep) {
			System.out.println("Language Error: " + namespace + " for " + player);
		}
		return namespace;
	}
	
	public static String get(Language code, String namespace) {
		return locales.get(code.toString()).getOrDefault(namespace, namespace);
	}
	
	public static void sendLanguageInfo(Player player) {
		player.sendMessage("§a您的默认语言以设置为 中文 (简体)。输入 /lang 以切换语言 §7[简体中文覆盖程度: 100%]");
		player.sendMessage("§a您的預設語言以設定為 中文 (簡體)。輸入 /lang 以切換語言 §7[繁體中文覆蓋程度: 0%]");
		player.sendMessage("§aYour default language has been set to Chinese (Simplified). Type in /lang to switch languages. §7[English (US) Coverage: 0%]");
	}
	
	public static void changeLanguage(Player player, Language lang) {
		
		JSONObject save = JSON.loadDataFromDataBase("players.json");
		JSONObject s2 = (JSONObject) save.get(player.getUniqueId().toString());
		s2.put("lang", lang.toString());
		save.put(player.getUniqueId().toString(), s2);
		JSON.saveDataFromDataBase("players.json", save);
		
	}

}