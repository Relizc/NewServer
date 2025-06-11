package net.itsrelizc.estate.marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.UUID;

import org.apache.logging.log4j.core.pattern.MarkerSimpleNamePatternConverter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Color;
import org.bukkit.HeightMap;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.yaml.snakeyaml.util.ArrayStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.nbt.ChunkMetadata;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.Hologram;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BorderMarker implements Listener {
	
	private static class BorderCoordinate {
		private int x;
		private int y;
		
		public BorderCoordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public boolean equals(int x, int y) {
			return this.x == x && this.y == y;
		}
		
		public int getChunkX() {
			return this.x >> 4;
		}
		
		public int getChunkY() {
			return this.y >> 4;
		}
	}
	
	private static Map<Player, List<BorderCoordinate>> working = new HashMap<Player, List<BorderCoordinate>>();
	private static Map<Player, BorderCoordinate> previous = new HashMap<Player, BorderCoordinate>();
	private static Map<Player, Integer> tasks = new HashMap<Player, Integer>();
	
	private static Map<Player, Map<Integer, List<Integer>>> collected = new HashMap<Player, Map<Integer, List<Integer>>>();
	
	public static class CommandBorderMarker extends RelizcCommand {

		public CommandBorderMarker() {
			super("marker", "mark lands");
		}
		
		public boolean onPlayerExecute(Player player, String[] args) {
			
			if (working.containsKey(player)) {
				working.remove(player);
				previous.remove(player);
				collected.remove(player);
				Bukkit.getScheduler().cancelTask(tasks.remove(player));
				player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0f);
				StringUtils.systemMessage(player, Locale.get(player, "marker.name"), Locale.get(player, "marker.cancelled"));
				return true;
			}
			
			working.put(player, new ArrayList<BorderCoordinate>());
			startTicking(player);
			
			Map<Integer, List<Integer>> d = new HashMap<Integer, List<Integer>>();
			collected.put(player, d);
			
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
			StringUtils.systemMessage(player, Locale.get(player, "marker.name"), Locale.get(player, "marker.start"));
			
			return true;
		}
		
		
		
	}
	
	public static int[] findAdjacentEdge(int x1, int y1, int x2, int y2) {
        // Ensure coordinates are multiples of 16 (chunk-aligned)
        if (x1 % 16 != 0 || y1 % 16 != 0 || x2 % 16 != 0 || y2 % 16 != 0) {
            return new int[]{}; // Return empty array on error
        }
        
        // Check horizontal adjacency
        if (Math.abs(x1 - x2) == 16 && y1 == y2) {
            int minX = Math.min(x1, x2);
            return new int[]{minX + 16, y1, minX + 16, y1 + 16};
        }
        
        // Check vertical adjacency
        if (Math.abs(y1 - y2) == 16 && x1 == x2) {
            int minY = Math.min(y1, y2);
            return new int[]{x1, minY + 16, x1 + 16, minY + 16};
        }
        
        // If not adjacent
        return new int[]{};
    }
	
	public static boolean eq(String a, String b) {
		if (a == null && b == null) return true;
		if (a == null || b == null) return false;
		return a.equals(b);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				Chunk cur = event.getPlayer().getLocation().getChunk();
		    	
		    	String owner = (String) ChunkMetadata.get(cur, "relizcPurchasedOwner", PersistentDataType.STRING);
		    	
		    	
		    	
		    	int[] deltax = {-1, 0, 1, 0};
		        int[] deltay = {0, -1, 0, 1};
		        
		        for (int i = 0; i < 4; i ++) {
		        	int nx = cur.getX() + deltax[i];
		        	int ny = cur.getZ() + deltay[i];
		        	
		        	Chunk nex = event.getPlayer().getLocation().getWorld().getChunkAt(nx, ny);
		        	String owner2 = (String) ChunkMetadata.get(nex, "relizcPurchasedOwner", PersistentDataType.STRING);
		        	
		        	String ownerName = (String) ChunkMetadata.get(cur, "relizcPurchasedPlotName", PersistentDataType.STRING);
		        	String owner2Name = (String) ChunkMetadata.get(nex, "relizcPurchasedPlotName", PersistentDataType.STRING);
		        	
		        	
		        	if (!eq(owner, owner2) || !eq(ownerName, owner2Name)) {
		        		//Bukkit.broadcastMessage(nex + " " + owner + " " + owner2);
		        		int[] coords = findAdjacentEdge(cur.getX() * 16, cur.getZ() * 16, nex.getX() * 16, nex.getZ() * 16);
		        		

		        		// displaying particle
		        		for (double x = coords[0]; x <= coords[2]; x += 0.3) {
		        			for (double y = coords[1]; y <= coords[3]; y += 0.3) {
		        				
			        			event.getPlayer().spawnParticle(Particle.FLAME, x, event.getPlayer().getWorld().getHighestBlockYAt((int) x, (int) y, HeightMap.MOTION_BLOCKING) + 1, y, 1, 0, 0, 0, 0);
			        		}
		        		}
		        	}
		        }
			}
			
		}, 0, 20L);
		
	}
	
	// PVP PROT
	@EventHandler
	public void attack(EntityDamageByEntityEvent event) {
		
		Chunk cur = event.getEntity().getLocation().getChunk();
		
		String ownerName = (String) ChunkMetadata.get(cur, "relizcPurchasedOwner", PersistentDataType.STRING);
		
		if (event.getEntity() instanceof Player) {
			Player damaged = (Player) event.getEntity();
			if (damaged.getUniqueId().toString().equals(ownerName)) {
				event.setCancelled(true);
				
				if (event.getDamager() instanceof Player) {
					Player attacker = (Player) event.getDamager();
					
					attacker.sendMessage(Locale.get(attacker, "plot.pvpdisabled"));
					attacker.playSound(attacker, Sound.BLOCK_FIRE_EXTINGUISH, 2f, 1f);
				}
			}
			
		}
		
		if (ownerName != null) {
			
			if (event.getDamager().getUniqueId().toString().equals(ownerName)) return;
			
			if (event.getDamager() instanceof Player) {
				Player attacker = (Player) event.getDamager();
				
				attacker.sendMessage(Locale.get(attacker, "plot.pvpdisabled"));
				attacker.playSound(attacker, Sound.BLOCK_FIRE_EXTINGUISH, 2f, 1f);
				event.setCancelled(true);
			}
		}
	}
	
	// INTERACTION AND CHEST PREVENTATION
//	@EventHandler
//	public void attack(PlayerInteractEvent event) {
//		
//		if (event.getClickedBlock() == null) return;
//		
//		Chunk cur = event.getClickedBlock().getLocation().getChunk();
//		
//		String ownerName = (String) ChunkMetadata.get(cur, "relizcPurchasedOwner", PersistentDataType.STRING);
//		
//		if (ownerName != null) {
//			
//			if (event.getPlayer().getUniqueId().toString().equals(ownerName)) return;
//
//				
//			event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "plot.interactdisabled"));
//			event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_FIRE_EXTINGUISH, 2f, 1f);
//			event.setCancelled(true);
//
//		}
//	}
	
	public static void startTicking(Player player) {
		
//		Hologram a = Hologram.spawn(player, player.getLocation(), Locale.get(player, "hologram.name"));
//		Hologram b = Hologram.spawn(player, player.getLocation(), Locale.get(player, "hologram.name"));
//		Hologram c = Hologram.spawn(player, player.getLocation(), Locale.get(player, "hologram.name"));
//		Hologram d = Hologram.spawn(player, player.getLocation(), Locale.get(player, "hologram.name"));
		
		int tt = Bukkit.getScheduler().scheduleSyncRepeatingTask(EventRegistery.main, new Runnable() {

			@Override
			public void run() {
				
				if (!player.isOnline()) {
					return;
				}
				
				for (int i = 0; i < working.get(player).size() - 1; i ++) {
					BorderCoordinate bc = working.get(player).get(i);
					BorderCoordinate bc2 = working.get(player).get(i + 1);
					
					drawline(player, bc, bc2);
				}
				
				if (working.get(player).size() == 0) {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.get(player, "marker.stepclose")));
					
					int chunkx = player.getLocation().getBlockX() >> 4;
			        int chunkz = player.getLocation().getBlockZ() >> 4;
			        
			        display(player, chunkx * 16, chunkz * 16, "def");
			        display(player, chunkx * 16 + 16, chunkz * 16, "def");
			        display(player, chunkx * 16, chunkz * 16 + 16, "def");
			        display(player, chunkx * 16 + 16, chunkz * 16 + 16, "def");
				} else {
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.get(player, "marker.stepadjancent")));
					
					BorderCoordinate prev = previous.get(player);
					
//					Bukkit.broadcastMessage(prev.x + " PREV " + prev.y);
					
					display(player, prev.x, prev.y + 16, "adj");
					display(player, prev.x, prev.y - 16, "adj");
					display(player, prev.x + 16, prev.y, "adj");
					
					
					int[] nx = {-16, 16};
					int[] ny = {-16, 16};
					
					
			        
			        for (BorderCoordinate coord : working.get(player)) {
//			        	if (coord.x == previous.get(player).x && coord.y == previous.get(player).y) continue;
			        	display(player, coord.x, coord.y, "prev");
			        }
			        
			        for (int x : nx) {
						if (collected.get(player).containsKey(prev.x + x) && collected.get(player).get(prev.x + x).contains(prev.y) && !working.get(player).get(0).equals(prev.x + x, prev.y)) continue;
						display(player, prev.x + x, prev.y, "adj");
					}
					
					for (int y : ny) {
						if (collected.get(player).containsKey(prev.x) && collected.get(player).get(prev.x).contains(prev.y+ y) && !working.get(player).get(0).equals(prev.x, prev.y + y)) continue;
						display(player, prev.x , prev.y+ y, "adj");
					}

				}
			}
			
		}, 0L, 20L);
		
		tasks.put(player, tt);
		
	}
	
	private static void drawline(Player player, BorderCoordinate a, BorderCoordinate b) {
		

		
		double dx = b.x - a.x;
		double dy = b.y - a.y;
		
		for (double t = 0; t <= 1; t += 0.02) {
			player.spawnParticle(Particle.FIREWORKS_SPARK, a.x + t * dx, player.getLocation().getWorld().getHighestBlockYAt((int) Math.ceil(a.x + t * dx), (int)Math.ceil(a.y + t * dy), HeightMap.MOTION_BLOCKING_NO_LEAVES) + 1, a.y + t * dy, 1, 0, 0, 0, 0);
		}
		
	}
	
	private static void display(Player player, int chunkx, int chunkz, String mode) {
		
//		if (Math.abs(chunkx - player.getLocation().getX()) * Math.abs(chunkx - player.getLocation().getX()) + Math.abs(chunkz - player.getLocation().getZ()) * Math.abs(chunkz - player.getLocation().getZ()) > )
		
		//Bukkit.broadcastMessage(chunkx + " " + chunkz);
		double height = player.getLocation().getWorld().getHighestBlockAt(chunkx, chunkz, HeightMap.MOTION_BLOCKING_NO_LEAVES).getY() + 1;
		
		if (collected.get(player).containsKey(chunkx) && collected.get(player).get(chunkx).contains(chunkz)) {
			if (previous.containsKey(player) && chunkx == previous.get(player).x && chunkz == previous.get(player).y) {
				player.spawnParticle(Particle.HEART, chunkx, height, chunkz, 10);
				
			} else if (chunkx == working.get(player).get(0).x && chunkz == working.get(player).get(0).y) {
				for (int t = 0; t <= 360; t += 360 / 32) {
					double x = Math.cos(Math.toRadians(t));
					double y = Math.sin(Math.toRadians(t));
					
					player.spawnParticle(Particle.VILLAGER_HAPPY, chunkx + x, height, chunkz + y, 1, null);
//					h.teleport(chunkx, height, chunkz);
				}
			} else {
				return;
			}
			
			
		}
		
		
		
		
		for (int t = 0; t <= 360; t += 360 / 32) {
			double x = Math.cos(Math.toRadians(t));
			double y = Math.sin(Math.toRadians(t));
			
			
			if (mode.equals("adj")) {
				player.spawnParticle(Particle.REDSTONE, chunkx + x, height, chunkz + y, 1, new Particle.DustOptions(Color.fromARGB(255, 255, 0, 255), 1));
			} else if (mode.equals("org")) {
				player.spawnParticle(Particle.REDSTONE, chunkx + x, height, chunkz + y, 1, new Particle.DustOptions(Color.fromARGB(255, 0, 0, 255), 1));
			} else {
				player.spawnParticle(Particle.VILLAGER_HAPPY, chunkx + x, height, chunkz + y, 1, null);
			}
//			h.teleport(chunkx, height, chunkz);
		}
		
		
		
		if (collected.get(player).containsKey(chunkx) && collected.get(player).get(chunkx).contains(chunkz)) {
			for (int t = 0; t <= 360; t += 360 / 32) {
				double x = 0.5 * Math.cos(Math.toRadians(t));
				double y = 0.5 * Math.sin(Math.toRadians(t));
				
				player.spawnParticle(Particle.CRIT, chunkx + x, height, chunkz + y, 1);
//				h.teleport(chunkx, height, chunkz);
			}
		}
		
		
		
		if (Math.abs(player.getLocation().getX() - chunkx) <= 2 && Math.abs(player.getLocation().getZ() - chunkz) <= 2) {
			
			if (mode.equals("adj")) {
				if (working.get(player).get(0).equals(chunkx, chunkz)) {
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.529732f);
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.667420f);
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 0.793701f);
					
					working.get(player).add(new BorderCoordinate(chunkx, chunkz));
					previous.put(player, new BorderCoordinate(chunkx, chunkz));

					if (collected.get(player).getOrDefault(chunkx, null) == null) {
						collected.get(player).put(chunkx, new ArrayList<Integer>());
					}
					collected.get(player).get(chunkx).add(chunkz);
					
					
					
					enclose(player);
					return;
				}
			}
			
			if (mode.equals("prev")) {
				if (working.get(player).get(0).equals(chunkx, chunkz)) {

				} else {
					return;
				}
			}
			
			if (collected.get(player).containsKey(chunkx) && collected.get(player).get(chunkx).contains(chunkz)) {

				if (chunkx != working.get(player).get(0).x || chunkz != working.get(player).get(0).y) {
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 0f);
					TaskDelay.delayTask(new Runnable() {

						@Override
						public void run() {
							player.playSound(player, Sound.BLOCK_NOTE_BLOCK_GUITAR, 1f, 0f);
						}}, 2L);
					StringUtils.systemMessage(player, Locale.get(player, "marker.name"), Locale.get(player, "marker.mustenclose") + " " + mode);
					
					
				}

				return;
			}

			StringUtils.systemMessage(player, Locale.get(player, "marker.name"), Locale.get(player, "marker.added"));
			
			
			working.get(player).add(new BorderCoordinate(chunkx, chunkz));
			previous.put(player, new BorderCoordinate(chunkx, chunkz));
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.get(player, "marker.stepadjancent")));
			player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1f, 2f);
			player.spawnParticle(Particle.LAVA, chunkx, height, chunkz, 30, 0, 0, 0);
			if (collected.get(player).getOrDefault(chunkx, null) == null) {
				collected.get(player).put(chunkx, new ArrayList<Integer>());
			}
			collected.get(player).get(chunkx).add(chunkz);
		}
		
		
		
		
	}
	
	private static void enclose(Player player) {
		Bukkit.getScheduler().cancelTask(tasks.get(player));
		//Bukkit.broadcastMessage("Lines: " + working.get(player).size());
		
		//showlace formula
		int n = working.get(player).size() - 1;
        double a = 0;
        
        List<BorderCoordinate> v = working.get(player);
        
        for (int i = 0; i < n - 1; i++) {
            a += v.get(i).x * v.get(i + 1).y - v.get(i + 1).x * v.get(i).y;
        }
        int area = (int) ((a + v.get(n - 1).x * v.get(0).y - v.get(0).x * v.get(n - 1).y) / 2);
        
        //Bukkit.broadcastMessage("Area: " + area);
        
        Map<Integer, HashSet<Integer>> outside = new HashMap<Integer, HashSet<Integer>>();
        
        BorderCoordinate inner = null;
        
        
        if (area < 0) { // counter righthandside (interior right side, mark left outside)
        	
        	for (int i = 0; i < v.size() - 1; i ++) {
        		BorderCoordinate cur = v.get(i);
        		BorderCoordinate nex = v.get(i + 1);
        		
        		BorderCoordinate outer = null;
        		
        		if (cur.getChunkX() == nex.getChunkX() && cur.getChunkY() < nex.getChunkY()) {
        			outer = new BorderCoordinate(cur.x - 16, cur.y); 
        			inner = new BorderCoordinate(cur.x, cur.y);
        		} else if (cur.getChunkX() == nex.getChunkX() && cur.getChunkY() > nex.getChunkY()) {
        			outer = new BorderCoordinate(nex.x, nex.y); //
        		} else if (cur.getChunkY() == nex.getChunkY() && cur.getChunkX() < nex.getChunkX()) {
        			outer = new BorderCoordinate(cur.x, cur.y);
        		} else if (cur.getChunkY() == nex.getChunkY() && cur.getChunkX() > nex.getChunkX()) {
        			outer = new BorderCoordinate(nex.x, nex.y - 16); // 
        		}
        		        		
        		//Bukkit.broadcastMessage(cur.getChunkX() + " " + cur.getChunkY() + " | " + nex.getChunkX() + " " + nex.getChunkY() + " | Outside: " + outer.getChunkX() + " " + outer.getChunkY());
        		
        		if (!outside.containsKey(outer.x)) outside.put(outer.x, new HashSet<Integer>());
        		outside.get(outer.x).add(outer.y);
        	}
        	
        } else { // clockwise
        	
        	
        	for (int i = 0; i < v.size() - 1; i ++) {
        		BorderCoordinate cur = v.get(i);
        		BorderCoordinate nex = v.get(i + 1);
        		
        		BorderCoordinate outer = null;
        		
        		if (cur.getChunkX() == nex.getChunkX() && cur.getChunkY() < nex.getChunkY()) {
        			outer = new BorderCoordinate(cur.x, cur.y); //
        			inner = new BorderCoordinate(cur.x - 16, cur.y); 
        		} else if (cur.getChunkX() == nex.getChunkX() && cur.getChunkY() > nex.getChunkY()) {
        			outer = new BorderCoordinate(nex.x - 16, nex.y); // 
        		} else if (cur.getChunkY() == nex.getChunkY() && cur.getChunkX() < nex.getChunkX()) {
        			outer = new BorderCoordinate(cur.x, cur.y - 16); // 
        		} else if (cur.getChunkY() == nex.getChunkY() && cur.getChunkX() > nex.getChunkX()) {
        			outer = new BorderCoordinate(nex.x, nex.y); //
        		}
        		        		
        		//Bukkit.broadcastMessage(cur.getChunkX() + " " + cur.getChunkY() + " | " + nex.getChunkX() + " " + nex.getChunkY() + " | Outside: " + outer.getChunkX() + " " + outer.getChunkY());
        		
        		if (!outside.containsKey(outer.x)) outside.put(outer.x, new HashSet<Integer>());
        		outside.get(outer.x).add(outer.y);
        	}
        	
        }
        
        Map<Integer, HashSet<Integer>> visited = new HashMap<Integer, HashSet<Integer>>();
        
        // Flood filling inner areas:
        String name = generateRandomLandName();
        StringUtils.systemMessage(player, Locale.get(player, "marker.name"), Locale.get(player, "marker.sucessclose", name));
        
        Stack<BorderCoordinate> chunks = new Stack<BorderCoordinate>();
        chunks.add(inner);
        
        int[] deltax = {-16, 0, 16, 0};
        int[] deltay = {0, -16, 0, 16};
        
        while (!chunks.empty()) {
        	BorderCoordinate current = chunks.pop();
        	
        	if (!visited.containsKey(current.x)) visited.put(current.x, new HashSet<Integer>());
        	visited.get(current.x).add(current.y);
        	
        	// do operation
        	//Bukkit.broadcastMessage("flooding " + current.getChunkX() + " " + current.getChunkY() + " Rest: " + chunks.size());
        	Chunk chun = player.getLocation().getWorld().getChunkAt(current.getChunkX(), current.getChunkY());
        	ChunkMetadata.set(chun, "relizcPurchasedOwner", PersistentDataType.STRING, player.getUniqueId().toString());
        	ChunkMetadata.set(chun, "chunkPermissionList", PersistentDataType.STRING, "");
			ChunkMetadata.set(chun, "chunkPermissionDigList", PersistentDataType.STRING, "");
			ChunkMetadata.set(chun, "chunkPermission", PersistentDataType.INTEGER, 0);
			ChunkMetadata.set(chun, "chunkPermissionDig", PersistentDataType.INTEGER, 0);
			ChunkMetadata.set(chun, "relizcPurchasedPlotName", PersistentDataType.STRING, name);
        	
        	for (int i = 0; i < 4; i ++) {
        		int nx = (current.x + deltax[i]);
        		int ny = (current.y + deltay[i]);
        		
        		if (!visited.containsKey(nx)) visited.put(nx, new HashSet<Integer>());
            	if (visited.get(nx).contains(ny)) {
            		continue;
            	}
            	if (!outside.containsKey(nx)) outside.put(nx, new HashSet<Integer>());
            	if (outside.get(nx).contains(ny)) {
            		continue;
            	}
            	
            	chunks.add(new BorderCoordinate(nx, ny));
        	}
        	
        	
//        	try {
//				Thread.sleep(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
        }
        
        
	}
	
	private static Random random = new Random();
	private static final String[] COMMON_CHINESE_CHARS = {
		        "天", "地", "玄", "黄", "宇", "宙", "洪", "荒", "日", "月",
		        "盈", "昃", "辰", "宿", "列", "张", "寒", "来", "暑", "往",
		        "秋", "收", "冬", "藏", "闰", "余", "成", "岁", "律", "吕",
		        "云", "腾", "致", "雨", "露", "结", "为", "霜", "金", "生",
		        "丽", "水", "玉", "出", "昆", "冈", "剑", "号", "巨", "阙"
		    };
	
	private static String generateRandomLandName() {
        // 从常用汉字中随机选取一个
        String randomChar = COMMON_CHINESE_CHARS[random.nextInt(COMMON_CHINESE_CHARS.length)];
        return randomChar + "州";
    }
	
	@EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        int fromChunkX = event.getFrom().getBlockX() >> 4;
        int fromChunkZ = event.getFrom().getBlockZ() >> 4;
        int toChunkX = event.getTo().getBlockX() >> 4;
        int toChunkZ = event.getTo().getBlockZ() >> 4;

        // Check if the chunk actually changed
        if (fromChunkX != toChunkX || fromChunkZ != toChunkZ) {
            
        	int minx = toChunkX * 16;
        	int minz = toChunkZ * 16;
        	int maxx = minx + 16;
        	int maxz = minz + 16;
        	
        	Chunk fro = event.getFrom().getChunk();
        	
        	Chunk cur = event.getTo().getChunk();
        	
        	
	    	
	    	String owner = (String) ChunkMetadata.get(cur, "relizcPurchasedOwner", PersistentDataType.STRING);
	    	String ownerName = (String) ChunkMetadata.get(cur, "relizcPurchasedPlotName", PersistentDataType.STRING);
	    	
	    	String owner3 = (String) ChunkMetadata.get(fro, "relizcPurchasedOwner", PersistentDataType.STRING);
	    	String owner3Name = (String) ChunkMetadata.get(fro, "relizcPurchasedPlotName", PersistentDataType.STRING);
	    	
	    	if (!eq(owner, owner3) || !eq(ownerName, owner3Name)) {
	    		if (owner == null) {
	    			OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(owner3));
		    		event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.get(event.getPlayer(), "plot.left", owner3Name, p.getName())));
		    		
		    		event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.890899f);
		    		TaskDelay.delayTask(new Runnable() {

						@Override
						public void run() {
							
							event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.707107f);
						}
		    			
		    		}, 5L);
		    		
	    		} else {
	    			OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(owner));
		    		event.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(Locale.get(event.getPlayer(), "plot.entered", ownerName, p.getName())));
		    		
		    		event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.707107f);
		    		TaskDelay.delayTask(new Runnable() {

						@Override
						public void run() {
							event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_HARP, 2f, 0.890899f);
						}
		    			
		    		}, 5L);
	    		}
	    		
	    	}
	    	

	    	
	    	
	    	int[] deltax = {-1, 0, 1, 0};
	        int[] deltay = {0, -1, 0, 1};
	        
	        for (int i = 0; i < 4; i ++) {
	        	int nx = cur.getX() + deltax[i];
	        	int ny = cur.getZ() + deltay[i];
	        	
	        	Chunk nex = event.getPlayer().getLocation().getWorld().getChunkAt(nx, ny);
	        	String owner2 = (String) ChunkMetadata.get(nex, "relizcPurchasedOwner", PersistentDataType.STRING);
	        	
	        	
	        	if (!eq(owner, owner3) || !eq(ownerName, owner3Name)) {
	        		//Bukkit.broadcastMessage(nex + " " + owner + " " + owner2);
	        		int[] coords = findAdjacentEdge(cur.getX() * 16, cur.getZ() * 16, nex.getX() * 16, nex.getZ() * 16);
	        		

	        		
	        		// displaying particle
	        		for (double x = coords[0]; x <= coords[2]; x += 0.3) {
	        			for (double y = coords[1]; y <= coords[3]; y += 0.3) {
	        				
		        			event.getPlayer().spawnParticle(Particle.FLAME, x, event.getPlayer().getWorld().getHighestBlockYAt((int) x, (int) y, HeightMap.MOTION_BLOCKING) + 1, y, 1, 0, 0, 0, 0);
		        		}
	        		}
	        	}
	        }
            
        }
    }
}
