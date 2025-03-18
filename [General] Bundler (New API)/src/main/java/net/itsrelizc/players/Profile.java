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
import net.itsrelizc.players.locales.Locale.Language;

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

	public long subscriptionEnd;
	
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
		
		this.lang = Language.ZH_CN;
		
		this.realUUID = player.getUniqueId();
		
		this.subscriptionEnd = -1;
		
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
			profiledata.put("subscription", -1);
			
			c.put(this.realUUID.toString(), profiledata);
			JSON.saveDataFromDataBase("players.json", c);
			
			Locale.sendLanguageInfo(this.owner);
			
		} else {
			profiledata = (JSONObject) c.get(this.realUUID.toString());
		}
		
		 
		
		
		
		this.permission = (long) profiledata.get("rank");
		this.level = (long) profiledata.get("rank");
		this.subscriptionEnd = (long) profiledata.getOrDefault("subscription", -1L);
		this.lang = net.itsrelizc.players.locales.Locale.Language.valueOf((String) profiledata.getOrDefault("lang", "EN_US"));

	}
	
	public static enum SubscriptionType {
		
		DAY(24l * 60l * 60l * 1000l),
		MONTH(30l * 24l * 60l * 60l * 1000l),
		WEEK(7l * 24l * 60l * 60l * 1000l),
		SEASON(3l * 30l * 24l * 60l * 60l * 1000l),
		YEAR(365l * 24l * 60l * 60l * 1000l);
		
		public long millis;

		private SubscriptionType(long millis) {
			this.millis = millis;
		}
		
	}
	
	public static void addSubscription(UUID uuid, SubscriptionType bundle) {
		
		JSONObject n = JSON.loadDataFromDataBase("players.json");
		JSONObject c = (JSONObject) n.get(uuid.toString());
		long cur = (long) c.get("subscription");
		cur += bundle.millis * 1000;
		c.put("subscription", cur);
		n.put(uuid.toString(), c);
		JSON.saveDataFromDataBase("players.json", n);
		
		Bukkit.broadcastMessage(String.valueOf(bundle.millis));
	}
	
	public static void addSubscription(UUID uuid, long bundle) {
//		
//		JSONObject n = JSON.loadDataFromDataBase("players.json");
//		JSONObject c = (JSONObject) n.get(uuid.toString());
//		long cur = (long) c.get("subscription");
//		cur += bundle.millis * 1000;
//		c.put("subscription", cur);
//		n.put(uuid.toString(), c);
//		JSON.saveDataFromDataBase("players.json", n);
	}
	
	public static void setSubscription(UUID uuid, SubscriptionType subscriptionType) {
		
		JSONObject n = JSON.loadDataFromDataBase("players.json");
		JSONObject c = (JSONObject) n.get(uuid.toString());
		long cur = System.currentTimeMillis();
		cur += subscriptionType.millis;
		c.put("subscription", cur);
		n.put(uuid.toString(), c);
		JSON.saveDataFromDataBase("players.json", n);
		
		Bukkit.broadcastMessage(String.valueOf(subscriptionType.millis));
	}
	
	public static void setSubscription(UUID uuid, long mils) {
		
		JSONObject n = JSON.loadDataFromDataBase("players.json");
		JSONObject c = (JSONObject) n.get(uuid.toString());
		long cur = System.currentTimeMillis();
		cur += mils * 1000;
		c.put("subscription", cur);
		n.put(uuid.toString(), c);
		JSON.saveDataFromDataBase("players.json", n);
	}
	
	public static Profile createProfile(Player player) {
		
		Profile a = new Profile(player);
		a.reloadProfile();
		
		
		
		return a;
	}
	
}
