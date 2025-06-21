package net.itsrelizc.gunmod.deathutils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.citizensnpcs.trait.SkinTrait;
import net.itsrelizc.gunmod.npcs.SleepingTrait;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.Rank;
import net.itsrelizc.players.locales.Locale;

public class DeathUtils {
	
	public static class PlayerGhostEvent extends Event {
	    private static final HandlerList HANDLERS = new HandlerList();
	    private final Player player;
	    private boolean cancelled;
		private boolean isGhost;
		private String cause;

	    public PlayerGhostEvent(Player player, boolean isGhost, String damageCause) {
	        this.player = player;
	        this.cancelled = false;
	        this.isGhost = isGhost;
	        this.cause = damageCause;
	    }

	    public Player getPlayer() {
	        return player;
	    }
	    
	    public boolean isGhost() {
	    	return isGhost;
	    }
	    
	    public String getCause() {
	    	return cause;
	    }

	    @Override
	    public HandlerList getHandlers() {
	        return HANDLERS;
	    }

	    public static HandlerList getHandlerList() {
	        return HANDLERS;
	    }
	}
	
	private static Set<String> deadppl = new HashSet<String>();
	
	private static Map<String, ItemStack[]> stuffz = new HashMap<String, ItemStack[]>();
	
	public static Team deadteam;
	
	public static boolean isDead(Player player) {
		return deadppl.contains(player.getUniqueId().toString());
		
		
	}
	
	public static Set<String> getDeadSet() {
		return deadppl;
	}
	
	public static void init() {
		
		if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam("nocollision") != null) {
			deadteam = Bukkit.getScoreboardManager().getMainScoreboard().getTeam("nocollision");
		} else {
			deadteam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("nocollision");
		}
		deadteam.setPrefix("§7[☠] ");
		deadteam.setColor(ChatColor.GRAY);
		deadteam.setCanSeeFriendlyInvisibles(true);
		deadteam.setOption(Option.COLLISION_RULE, Team.OptionStatus.NEVER);
	}
	
	private static Equipment.EquipmentSlot convertSlot(EquipmentSlot bukkit) {
		if (bukkit == EquipmentSlot.CHEST) return Equipment.EquipmentSlot.CHESTPLATE;
		else if (bukkit == EquipmentSlot.FEET) return Equipment.EquipmentSlot.BOOTS;
		else if (bukkit == EquipmentSlot.HAND) return Equipment.EquipmentSlot.HAND;
		else if (bukkit == EquipmentSlot.HEAD) return Equipment.EquipmentSlot.HELMET;
		else if (bukkit == EquipmentSlot.LEGS) return Equipment.EquipmentSlot.LEGGINGS;
		else if (bukkit == EquipmentSlot.OFF_HAND) return Equipment.EquipmentSlot.OFF_HAND;
		return Equipment.EquipmentSlot.BODY;
	}
	
	public static void addPlayer(Player player, String damageCause) {
		addPlayer(player, damageCause, null);
	}
	
	private static void spawnDeadBody(Player player, Player killer, String damageCause, Profile prof) {
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "body" + player.getName());
		npc.data().set(NPC.Metadata.NAMEPLATE_VISIBLE, false);
		npc.spawn(player.getLocation());
		SkinTrait skin = npc.getOrAddTrait(SkinTrait.class);
		skin.setSkinName(player.getName()); // uses Mojang's skin servers
		npc.addTrait(SleepingTrait.class);
		
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (player.getInventory().getItem(slot) != null) {
				npc.getOrAddTrait(Equipment.class).set(convertSlot(slot), player.getInventory().getItem(slot));
			}
		}
		
		Inventory cloned = Bukkit.createInventory(null, 45, player.getName() + "的尸体");
		
		
		String kp;
		if (killer != null) {
			kp = Profile.coloredName(killer);
		} else {
			kp = "item.RELIZC_PLAYER_HEAD.unknown";
		}
		
		long rank;
		if (prof == null) {
			rank = 256;
		} else {
			rank = prof.permission;
		}
		RelizcOverridedPlayerHead item = ItemUtils.createItem(RelizcOverridedPlayerHead.class, player, new MetadataPair("DEATH_TIME", System.currentTimeMillis()), new MetadataPair("WEAPON_DISPLAY", damageCause), new MetadataPair("KILLER_DISPLAY", kp), new MetadataPair("OWNER_NAME", player.getName()), new MetadataPair("OWNER_RANK", rank));
		

		// Clone all items
		ItemStack[] contents = player.getInventory().getContents();
		//cloned.setContents(contents.clone());
		for (int i = 36; i <= 40; i ++) {
			cloned.setItem(i - 36, contents[i]);
		}
		cloned.setItem(8, item.getBukkitItem());
		
		for (int i = 9; i <= 44; i ++) {
			cloned.setItem(i, contents[i - 9]);
		}
		
		npc.data().set("inventory", cloned);
		npc.data().set("player", player.getUniqueId());
		npc.data().set("craftplayer", player);
		npc.data().set("name", player.getName());
	}
	
	public static void addPlayer(Player player, String damageCause, Player killer) {
		
		//Bukkit.broadcastMessage("added " + player);
		if (Profile.findByOwner(player) == null) {
			
			spawnDeadBody(player, killer, damageCause, null);
			player.teleport(new Location(player.getLocation().getWorld(), 0, -256, 0));
			player.setHealth(0);
			return;
			
		}
		
		if (damageCause == null) {
			damageCause = "damage.unknown";
		}
		
		if (deadppl.contains(player.getUniqueId().toString())) return;
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (DeathUtils.isDead(p)) {
				p.showPlayer(player);
			} else {
				p.hidePlayer(player);
			}
			
		}
		deadppl.add(player.getUniqueId().toString());
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
		player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20 * 3, 1, false, false));
		
		player.setAllowFlight(true);
		player.setFlying(true);
		
		player.playSound(player, Sound.ENTITY_WITHER_SPAWN, 1f, 0f);
		for (int i = 0; i < 3; i ++ ) player.playSound(player, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1f, 0f);;
		
		
		
		
		spawnDeadBody(player, killer, damageCause, Profile.findByOwner(player));
		
		deadteam.addEntry(player.getName());
		
		player.getInventory().clear();
		
		player.setCanPickupItems(false);
		
		player.setSaturation(20);
		player.setFoodLevel(20);
		player.setHealth(20);

		
		
		
		
		PlayerGhostEvent event = new PlayerGhostEvent(player, true, damageCause);
		Bukkit.getPluginManager().callEvent(event);
		
	}
	
	public static void removePlayer(Player player) {
		
		if (!deadppl.contains(player.getUniqueId().toString())) return;
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (DeathUtils.isDead(p)) {
				p.hidePlayer(player);
			} else {
				p.showPlayer(player);
			}
		}
		deadppl.remove(player.getUniqueId().toString());
		
		player.removePotionEffect(PotionEffectType.INVISIBILITY);
		player.removePotionEffect(PotionEffectType.NIGHT_VISION);
		player.removePotionEffect(PotionEffectType.BLINDNESS);
		
		player.setAllowFlight(false);
		player.setFlying(false);
		
		player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, 1f, 2f);
		
		if (stuffz.containsKey(player.getUniqueId().toString())) {
			ItemStack[] itemz = stuffz.get(player.getUniqueId().toString());
			stuffz.remove(player.getUniqueId().toString());
			
			for (int i = 0; i < itemz.length; i ++) {
				player.getInventory().setItem(i, itemz[i]);
			}
		}
		
		deadteam.addEntry(player.getName());
		
		player.setCanPickupItems(true);
		
		PlayerGhostEvent event = new PlayerGhostEvent(player, false, null);
		Bukkit.getPluginManager().callEvent(event);
	}

	public static void addPlayer(Player owner) {
		addPlayer(owner, "damage.kill");
	}

}
