package net.itsrelizc.players.locales;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.Profile;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class Locale {
	
	public static enum Language {
		EN_US,
		ZH_CN,
		ZH_TW, JA_JP, KO_KR, ES_ES
		
	}
	
	public static HashMap<String, HashMap<String, String>> locales = new HashMap<String, HashMap<String, String>>();
	
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
	
	public static int getLinesAmount(Language code) {
		return locales.get(code.toString()).size();
	}
	
	public static String get(Player player, String namespace) {

		return locales.get(Profile.findByOwner(player).lang.toString()).getOrDefault(namespace, namespace);

	}
	
	public static Language getLanguage(Player player) {
		return Profile.findByOwner(player).lang;
	}
	
	public static ItemStack insertSmartLocale(ItemStack it, String localizedName, String... localizedLore) {
		
		NBTTagCompound tag = NBT.getNBT(it);
		if (tag == null) tag = new NBTTagCompound();
		
		NBTTagCompound languages = new NBTTagCompound();
		NBT.setString(languages, "name", localizedName);
		
		NBTTagList list = new NBTTagList();
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
