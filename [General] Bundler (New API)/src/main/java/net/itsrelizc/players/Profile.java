package net.itsrelizc.players;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.players.locales.Locale;

public class Profile {
	
	public Player owner;
	
	public String name;
	public String displayName;
	public List<String> nameHistory;
	
	public long permission;
	public boolean useAdvancedNameDisplay;
	public String advancedNameDisplay;
	
	public UUID realUUID;
	
	public long level;

	public net.itsrelizc.players.locales.Locale.Language lang;
	
	public static List<Player> awaitCreation = new ArrayList<Player>();
	public static List<Player> awaitSetUUID = new ArrayList<Player>();
	
	private static List<Profile> profiles = new ArrayList<Profile>();
	
	public Profile(Player player) {
		this.owner = player;
		
		this.displayName = player.getDisplayName();
		this.name = player.getName();
		
//		this.nameHistory = (List<String>) profiledata.get("general-namehist");
//		
//		this.permission = (long) profiledata.get("general-rank");
//		this.level = (long) profiledata.get("general-rank");
//		
//		this.realUUID = null;
		
		this.nameHistory = null;
		
		this.permission = -1L;
		this.level = -1L;
		
		this.lang = null;
		
		this.realUUID = player.getUniqueId();
		
		addProfile(this);
	}
	
	public static String coloredName(Player player) {
		Profile p = findByOwner(player);
		return Rank.findByPermission(p.permission).displayName.substring(0, 2) + player.getDisplayName();
	}
	
	public static Profile findByOwner(Player player) {
		for (Profile p : profiles) {
			if (p.owner.getName().equalsIgnoreCase(player.getName())) {
				return p;
			}
		}
		
		return null;
	}
	
	public static Profile findByOwner(String player) {
		for (Profile p : profiles) {
			if (p.owner.getName().equalsIgnoreCase(player)) {
				return p;
			}
		}
		
		return null;
	}
	
	public static void addProfile(Profile profile) {
		profiles.add(profile);
	}
	
	public static void removeProfile(Profile profile) {
		profiles.remove(profile);
	}
	
	public static boolean checkAccountExists(Player player) {
		
		JSONObject j = JSON.loadDataFromDataBase("players.json");
		
		
		
		return j.containsKey(player.getUniqueId().toString());
	
	}
	
	public void reloadProfile() {
		JSONObject c = JSON.loadDataFromDataBase("players.json");
		JSONObject profiledata;
		
		if (!c.containsKey(this.realUUID.toString())) {
			
			profiledata = new JSONObject();
			profiledata.put("rank", 1L);
			profiledata.put("lang", "ZH_CN");
			
			c.put(this.realUUID.toString(), profiledata);
			JSON.saveDataFromDataBase("players.json", c);
			
			Locale.sendLanguageInfo(this.owner);
			
		} else {
			profiledata = (JSONObject) c.get(this.realUUID.toString());
		}
		
		 
		
		
		
		this.permission = (long) profiledata.get("rank");
		this.level = (long) profiledata.get("rank");
		this.lang = net.itsrelizc.players.locales.Locale.Language.valueOf((String) profiledata.getOrDefault("lang", "EN_US"));

	}
	
	public static Profile createProfile(Player player) {
		
		Profile a = new Profile(player);
		a.reloadProfile();
		
		
		
		return a;
	}
	
}
