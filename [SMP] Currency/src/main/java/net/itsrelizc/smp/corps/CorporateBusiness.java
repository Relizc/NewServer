package net.itsrelizc.smp.corps;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.corps.Contract.Party;
import net.itsrelizc.smp.corps.Contract.Party.PartyType;
import net.itsrelizc.smp.corps.api.Business;

public class CorporateBusiness implements Business {
	
	public static enum BusinessLiabiliy {
		LIMITED((int) 0x00, "business.liability.limited"),
		UNLIMITED((int) 0x01, "business.liability.unlimited"),
		GOVERNMENT((int) 0x03, "business.liability.government");
		
		public int type;
		private String namespace;

		private BusinessLiabiliy(int type, String namespace) {
			this.type = type;
			this.namespace = namespace;
		}
		
		public String getNamespace() {
			// TODO Auto-generated method stub
			return this.namespace;
		}
	}

	public static CorporateBusiness loadCompany(String string) {
		
		Path path = Paths.get(JSON.PREFIX + "company/" + string);
		
		if (Files.exists(path) && Files.isDirectory(path)) {
			
			return new CorporateBusiness(string);
			
		} else {
			return null;
		}
		
	}
	
	public static class Share {
		
		private OfflinePlayer owner;
		private double hold;

		public Share(OfflinePlayer owner2, double hold) {
			this.owner = owner2;
			this.hold = hold;
		}
		
	}
	
	private static Map<String, CorporateBusiness> businessCache = new HashMap<String, CorporateBusiness>();
	
	private HashMap<String, Map<String, String>> locales;
	private HashMap<OfflinePlayer, Share> shares;
	private PartyType type;
	private String name;
	private BusinessLiabiliy liability;
	private String secret;
	private UUID id;

	private String registration;

	@SuppressWarnings("unchecked")
	private CorporateBusiness(String string) {
		
		this.registration = string;
		
		locales = new HashMap<String, Map<String, String>>();
		
		JSONObject loc = JSON.loadDataFromDataBase("company/" + string + "/locale.json");
		for (Object language : loc.keySet()) {
			
			locales.put((String) language, (Map<String, String>) loc.get(language));
			
			
		}
		
		JSONObject registration = JSON.loadDataFromDataBase("company/" + string + "/registration.json");
		
		this.name = (String) registration.get("name");
		this.type = PartyType.valueOf((String) registration.get("type"));
		this.liability = BusinessLiabiliy.valueOf((String) registration.get("liability"));
		this.secret = (String) registration.get("secret");
		this.id = UUID.fromString((String) registration.get("id"));
		
		this.shares = new HashMap<OfflinePlayer, Share>();
		
		JSONObject shareholders = (JSONObject) registration.get("shareholders"); 
		for (Object set : shareholders.keySet()) {
			
			UUID who = UUID.fromString((String) set);
			OfflinePlayer owner = (OfflinePlayer) Bukkit.getOfflinePlayer(who);
			double share = (double) shareholders.get(set);
			
			this.shares.put(owner, new Share(owner, share));
			
		}
		
		businessCache.put(string, this);
		
	}
	
	
	public static CorporateBusiness getByID(String id) {
		
		CorporateBusiness result = businessCache.getOrDefault(id, null);
		
		if (result == null) {
			CorporateBusiness add = new CorporateBusiness(id);
			businessCache.put(id, add);
			return add;
		}
		
		return result;
	}
	
	@SuppressWarnings("deprecation")
	public static void startClearCache() {
		
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				System.out.println("Cleared Business Cache");
				businessCache.clear();
			}
			
		}, 72000L, 72000L);
		
	}
	
	private String get(Player player, String namespace) {

		return locales.get(Profile.findByOwner(player).lang.toString()).getOrDefault(namespace, namespace);

	}


	public String getName() {
		return this.name;
	}
	
	public String getName(Player player) {
		return get(player, this.name);
	}
	
	public PartyType getType() {
		return this.type;
	}
	
	public String getTypeName(Player player) {
		return Locale.get(player, this.type.getNamespace());
	}
	
	public String getLiabilityName(Player player) {
		return Locale.get(player, this.liability.getNamespace());
	}
	
	public String getRegistrationName() {
		return this.registration;
	}
	
	public String getDisplayName(Player player) {
		return getName(player) + getLiabilityName(player) + getTypeName(player);
	}
	
	public String getLoreName(Player player) {
		return "[" + getLiabilityName(player) + getTypeName(player) + "] " + getName(player);
	}
	
	public String getLocale(Player player, String namespace) {
		return get(player, namespace);
	}


	public Party asParty(boolean b) {
		return new Party(this.type, this, b);
	}


	public static Business getIndividual(Player player) {
		return new IndividualBusiness(player);
	}


	
	

}
