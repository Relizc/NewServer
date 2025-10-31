package net.itsrelizc.gunmod.deathutils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.trait.Equipment;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.health2.Body;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.players.CustomPlayerTeleportEvent;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.minecraft.world.entity.Pose;

public class DeathUtils {
	
	public static class PlayerGhostEvent extends Event {
	    private static final HandlerList HANDLERS = new HandlerList();
	    private final Player player;
	    private boolean cancelled;
		private boolean isGhost;
		private String cause;
		private LivingEntity killer;

	    public PlayerGhostEvent(Player player, LivingEntity killer, boolean isGhost, String damageCause) {
	        this.player = player;
	        this.killer = killer;
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
	    
	    public LivingEntity getKiller() {
			return killer;
	    	
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
	
	private static void spawnDeadBody(Player player, LivingEntity killer, String damageCause, Profile prof) {
		
		String actualName = player.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "bodied_name"), PersistentDataType.STRING);
		if (actualName == null) {
			actualName = player.getName();
		}
		
		
		NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "=body" + player.getName());
		npc.data().set(NPC.Metadata.NAMEPLATE_VISIBLE, false);
		npc.spawn(player.getLocation());
		
		Player bukkitPlayer = (Player) npc.getEntity();
		net.minecraft.world.entity.player.Player nmsPlayer = ((CraftPlayer) bukkitPlayer).getHandle();
		
		net.minecraft.world.entity.player.Player originalPlayer = ((CraftPlayer) player).getHandle();
		GameProfile original = originalPlayer.getGameProfile();
		Collection<Property> ppts = original.getProperties().get("textures");
		if (ppts.size() > 0) {
			Property copied = ppts.iterator().next();
			String texture = copied.getValue();
	        String signature = copied.getSignature();


	        // Get the GameProfile
	        GameProfile profile = nmsPlayer.getGameProfile();
	        profile.getProperties().put("textures", new Property("textures", texture, signature));
		}
		
		nmsPlayer.setPose(Pose.SLEEPING);
		
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			if (player.getInventory().getItem(slot) != null) {
				npc.getOrAddTrait(Equipment.class).set(convertSlot(slot), player.getInventory().getItem(slot));
			}
		}
		
		Inventory cloned = Bukkit.createInventory(null, 45, actualName + "的尸体");
		
		
		String kp;
		if (killer != null) {
			if (killer instanceof Player) {
				if (Profile.findByOwner((Player) killer) == null) {
					String NPCactualName = killer.getPersistentDataContainer().get(new NamespacedKey(EventRegistery.main, "bodied_name"), PersistentDataType.STRING);
					if (NPCactualName == null) {
						kp = player.getName();
					} else {
						kp = NPCactualName;
					}
				} else {
					kp = Profile.coloredName((Player) killer);
				}
			} else {
				kp = killer.getName();
			}
		} else {
			kp = "item.RELIZC_PLAYER_HEAD.unknown";
		}
		
		long rank;
		if (prof == null) {
			rank = 256;
		} else {
			rank = prof.permission;
		}
		

		RelizcOverridedPlayerHead item = ItemUtils.createItem(RelizcOverridedPlayerHead.class, player, new MetadataPair("DEATH_TIME", System.currentTimeMillis()), new MetadataPair("WEAPON_DISPLAY", damageCause), new MetadataPair("KILLER_DISPLAY", kp), new MetadataPair("OWNER_NAME", actualName), new MetadataPair("OWNER_RANK", rank));
		

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
	
	private static HashMap<String, Location> deathlocations = new HashMap<String, Location>();
	
	public static void addPlayer(Player player, String damageCause, LivingEntity killer) {
		
		////("added " + player);
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
		deathlocations.put(player.getUniqueId().toString(), player.getLocation());
		
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

		
		
		
		
		PlayerGhostEvent event = new PlayerGhostEvent(player, killer, true, damageCause);
		Bukkit.getPluginManager().callEvent(event);
		
	}
	
	public static void removePlayer(Player player) {

		
		if (!deadppl.contains(player.getUniqueId().toString())) return;
		Body body = Body.parts.get(player.getUniqueId().toString());
		
		if (body.convert(0).getHealth() <= 0) {
			body.convert(0).setHealth(1);
		}
		if (body.convert(1).getHealth() <= 0) {
			body.convert(1).setHealth(1);
		}
		
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
		
		deadteam.removeEntry(player.getName());
		
		
		player.setCanPickupItems(true);
		player.getInventory().clear();
		
		
		Location loc = findRandomSpawnableLocation(player, deathlocations.get(player.getUniqueId().toString()));
		player.teleport(loc);
		
		
		
//		player.teleport(bed);
		//CustomPlayerTeleportEvent.teleport(player, bed);
		player.setFallDistance(0);
		body.refreshHealthDisplay();
		
		PlayerGhostEvent event = new PlayerGhostEvent(player, null, false, null);
		Bukkit.getPluginManager().callEvent(event);
	}
	
	private static final int MAX_RADIUS = 64;

    public static Location findRandomSpawnableLocation(Player player, Location center) {
        World world = center.getWorld();
        Random random = new Random();

        for (int i = 0; i < 200; i++) { // Try up to 200 random positions
            int dx = random.nextInt(MAX_RADIUS * 2) - MAX_RADIUS;
            int dz = random.nextInt(MAX_RADIUS * 2) - MAX_RADIUS;

            int x = center.getBlockX() + dx;
            int z = center.getBlockZ() + dz;

            int y = world.getHighestBlockYAt(x, z); // get surface height
            if (y < world.getSeaLevel()) continue; // skip below sea level

            Block block = world.getBlockAt(x, y - 1, z); // block below surface
            if (block.getType().isSolid()) {
                Location spawnLoc = new Location(world, x + 0.5, y + 1, z + 0.5);
                // ensure it's not inside a liquid or unsafe block
                Material above = world.getBlockAt(x, y + 1, z).getType();
                Material above2 = world.getBlockAt(x, y + 2, z).getType();
                if ((above == Material.AIR || above == Material.CAVE_AIR) && (above2 == Material.AIR || above2 == Material.CAVE_AIR)) {
                	player.sendMessage(Locale.a(player, "death.spawnnear"));
                    return spawnLoc;
                }
            }
        }

        // Fallback: player bed spawn or world spawn
        Location bed = player.getBedSpawnLocation();
        if (bed != null) {
        	player.sendMessage(Locale.a(player, "death.noloc"));
        	return bed;
        }
        
        player.sendMessage(Locale.a(player, "death.nobed"));
        return world.getSpawnLocation();
    }

	public static void addPlayer(Player owner) {
		addPlayer(owner, "damage.kill");
	}

}
