package net.itsrelizc.estate.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.persistence.PersistentDataType;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.nbt.ChunkMetadata;
import net.itsrelizc.players.locales.Locale;

public class EstateListener implements Listener {
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		
		
		
		Chunk current = event.getBlock().getLocation().getChunk();
		
		if (ChunkMetadata.get(current, "relizcPurchasedOwner", PersistentDataType.STRING) != null && ChunkMetadata.get(current, "chunkPermission", PersistentDataType.INTEGER) == null) { //backwards compatibility
			ChunkMetadata.set(current, "chunkPermissionList", PersistentDataType.STRING, "");
			ChunkMetadata.set(current, "chunkPermissionDigList", PersistentDataType.STRING, "");
			ChunkMetadata.set(current, "chunkPermission", PersistentDataType.INTEGER, 0);
			ChunkMetadata.set(current, "chunkPermissionDig", PersistentDataType.INTEGER, 0);
			
		}
		
		if (ChunkMetadata.get(current, "relizcPurchasedOwner", PersistentDataType.STRING) == null) {
			ChunkMetadata.remove(current, "chunkPermission");
			ChunkMetadata.remove(current, "chunkPermissionDig");
		}
		
		if (ChunkMetadata.get(current, "chunkPermissionDig", PersistentDataType.INTEGER) != null && (int) ChunkMetadata.get(current, "chunkPermissionDig", PersistentDataType.INTEGER) == 0 && !ChunkMetadata.get(current, "relizcPurchasedOwner", PersistentDataType.STRING).equals(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c" + Locale.get(event.getPlayer(), "globalestate.dig.deny"));
			event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
			return;
		}
		
		
				
		Integer cow = (Integer) ChunkMetadata.get(current, "chunkPermissionDig", PersistentDataType.INTEGER);
		
		if (ChunkMetadata.get(current, "chunkPermissionDig", PersistentDataType.INTEGER) != null && (cow == 1 || cow == 2)) {
			
			String list = (String) ChunkMetadata.get(current, "chunkPermissionDigList", PersistentDataType.STRING);
			String[] args = list.split("\n");
			
			if (cow == 1) {
				
			}
		}
		
	}
	
	@EventHandler
	public void onBreak(PlayerInteractEvent event) {
		
		if (event.getPlayer().getInventory().getItemInOffHand() == null) return;
		
		Chunk current = event.getClickedBlock().getLocation().getChunk();
		
		if (ChunkMetadata.get(current, "relizcPurchasedOwner", PersistentDataType.STRING) != null && ChunkMetadata.get(current, "chunkPermissionDig", PersistentDataType.INTEGER) == null) { //backwards compatibility
			ChunkMetadata.set(current, "chunkPermissionList", PersistentDataType.STRING, "");
			ChunkMetadata.set(current, "chunkPermissionDigList", PersistentDataType.STRING, "");
			ChunkMetadata.set(current, "chunkPermission", PersistentDataType.INTEGER, 0);
			ChunkMetadata.set(current, "chunkPermissionDig", PersistentDataType.INTEGER, 0);
			
		}
		
		if (ChunkMetadata.get(current, "relizcPurchasedOwner", PersistentDataType.STRING) == null) {
			ChunkMetadata.remove(current, "chunkPermission");
			ChunkMetadata.remove(current, "chunkPermissionDig");
		}
		
		if (ChunkMetadata.get(current, "chunkPermission", PersistentDataType.INTEGER) != null && (int) ChunkMetadata.get(current, "chunkPermission", PersistentDataType.INTEGER) == 0 && !ChunkMetadata.get(current, "relizcPurchasedOwner", PersistentDataType.STRING).equals(event.getPlayer().getUniqueId().toString())) {
			event.setCancelled(true);
			event.getPlayer().sendMessage("§c" + Locale.get(event.getPlayer(), "globalestate.interact.deny"));
			event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
		}
		
	}
	
	public static class ChunkInfo {
		
		private int x;
		private int y;

		public ChunkInfo(int coordx, int coordy) {
			this.x = coordx;
			this.y = coordy;
		}
		
	}
	
	public static Map<OfflinePlayer, JSONObject> chunkmaps = new HashMap<OfflinePlayer, JSONObject>();
	
	@EventHandler
	public void join(PlayerLoginEvent event) {
		loadPlayerMapping(event.getPlayer());
	}
	
	@EventHandler
	public void join(PlayerQuitEvent event) {
		savePlayerMapping(event.getPlayer());
	}
	
	public static void loadPlayerMapping(Player player) {
		
		JSONObject stuff = JSON.loadDataFromDataBase("chunk_claims/" + player.getUniqueId().toString() + ".json");
		
		chunkmaps.put(player, stuff);
		
	}
	
	public static void savePlayerMapping(Player player) {
		
		JSON.saveDataFromDataBase("chunk_claims/" + player.getUniqueId().toString() + ".json", chunkmaps.get(player));
		chunkmaps.remove(player.getUniqueId().toString());
		
	}

}
