package net.itsrelizc.maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.CustomPlayerTeleportEvent;
import net.itsrelizc.players.locales.Locale;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class MapImageViewer implements Listener {
	
	public static void enable() {
		EventRegistery.register(new MapImageViewer());
		
		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player player : AS.keySet()) {
					
				}
			}
			
		}.runTaskTimer(EventRegistery.main, 0, 20l);
	}
	
	public static void spawnMapFrameWithView(Player player) {
		LOC.put(player, player.getLocation());
	    World world = player.getWorld();
	    Location center = player.getLocation().add(0, 2, 5); // 5 blocks ahead

	    // --- Create map ---
	    MapView mapView = Bukkit.createMap(world);
	    mapView.getRenderers().clear();
	    mapView.addRenderer(new HighlightChunkMapRenderer(EventRegistery.main));
	    player.sendMap(mapView);
	    
	    ItemStack original = player.getItemInHand();
	    IT.put(player, original);
	    
	    ItemStack mapItem = new ItemStack(Material.FILLED_MAP);
	    MapMeta meta = (MapMeta) mapItem.getItemMeta();
	    meta.setMapView(mapView);
	    mapItem.setItemMeta(meta);
	    
	    player.setItemInHand(mapItem);

	    // --- Spawn item frame ---
	    Location frameLocation = center.clone(); // This will be the back of the item frame
	    frameLocation.setYaw(180); // Facing toward the player
	    ItemFrame frame = (ItemFrame) world.spawnEntity(frameLocation, EntityType.ITEM_FRAME);
	    frame.setFacingDirection(BlockFace.SOUTH, true); // Or another direction if needed
	    frame.setFixed(true);
	    frame.setItem(mapItem);
	    frame.setRotation(Rotation.NONE);
	    frame.setVisible(false);
	    IF.put(player, frame);

	    // --- Spawn armor stand ---
	    Location armorStandLocation = frameLocation.clone(); // 2 blocks in front of the frame
	    armorStandLocation.setX(armorStandLocation.getBlockX() + 0.5);
	    armorStandLocation.setY(armorStandLocation.getBlockY() + 0.5);
	    armorStandLocation.setZ(frameLocation.getBlockZ() + 1);
	    armorStandLocation.setPitch(0);
	    armorStandLocation.setYaw(180);
	    //armorStandLocation.setDirection(frameLocation.toVector().subtract(armorStandLocation.toVector())); // Face the map

	    ArmorStand stand = (ArmorStand) world.spawnEntity(armorStandLocation, EntityType.ARMOR_STAND);
	    stand.setVisible(false);
	    stand.setMarker(true); // No hitbox
	    stand.setGravity(false);
	    stand.setSilent(true);
	    AS.put(player, stand);
	    
	    for (Player p : Bukkit.getOnlinePlayers()) {
	    	if (p.getUniqueId().toString().equals(player.getUniqueId().toString())) {
	    		
	    	} else {
	    		p.hideEntity(EventRegistery.main, stand);
	    		p.hideEntity(EventRegistery.main, frame);
	    	}
	    }
	    
	    COUNT.add(player.getUniqueId().toString());

	    // --- Spectate the armor stand ---
	    player.setGameMode(GameMode.SPECTATOR);
	    player.setSpectatorTarget(stand);
	    
	    
	    
	    BukkitTask task = new BukkitRunnable() {
	    	
	    	int cycle = 0;

			@Override
			public void run() {
				cycle ++;
				if (cycle % 2 == 0) {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.a(player, "estate.map.quit")));
				} else {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.a(player, "estate.map.legend")));
				}
			}
	    	
	    }.runTaskTimer(EventRegistery.main, 0l, 40l);
	    TASK.put(player, task);
	}
	
	private static Map<Player, ArmorStand> AS = new HashMap<Player, ArmorStand>();
	private static Map<Player, ItemFrame> IF = new HashMap<Player, ItemFrame>();
	private static Map<Player, Location> LOC = new HashMap<Player, Location>();
	private static Map<Player, ItemStack> IT = new HashMap<Player, ItemStack>();
	private static Map<Player, BukkitTask> TASK = new HashMap<Player, BukkitTask>();
	
	
	private static Set<String> COUNT = new HashSet<String>();
	
	public static void disable() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			cancelFor(player);
		}
	}
	
	public static void cancelFor(Player player) {
		if (AS.containsKey(player)) {
            ArmorStand stand = AS.get(player);
            ItemFrame frame = IF.get(player);
            Location org = LOC.get(player);
            ItemStack it = IT.get(player);
            BukkitTask task = TASK.get(player);
            AS.remove(player); IF.remove(player); LOC.remove(player); IT.remove(player); COUNT.remove(player.getUniqueId().toString()); TASK.remove(player);
            
            stand.remove();
            frame.remove();
            player.setItemInHand(it);
            //player.teleport(org);
            player.setFallDistance(0);
            //player.teleport(new Location(player.getWorld(), org.getX(), org.getY(), org.getZ(), org.getYaw(), org.getPitch()));
            CustomPlayerTeleportEvent.teleport(player, new Location(player.getWorld(), org.getX(), org.getY(), org.getZ(), org.getYaw(), org.getPitch()));
            
            
            task.cancel();
            
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(" "));
            player.setGameMode(GameMode.SURVIVAL);
            
        }
	}

	@EventHandler
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
	    Player player = event.getPlayer();
	    if (player.getGameMode() == GameMode.SPECTATOR && player.getSpectatorTarget() == null) {
	        // Player has exited spectator mode
	        // Check if the player was spectating the specific armor stand
	    	cancelFor(player);
	    }
	}
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEvent event) {
		Player player = event.getPlayer();
	    if (COUNT.contains(player.getUniqueId().toString())) {
	    	event.setCancelled(true);
	    }
	}
}
