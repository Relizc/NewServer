package net.itsrelizc.diamonds;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import net.itsrelizc.bundler.JSON;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.string.StringUtils;

public class DiamondCounter {
	
	private static class LootGeneratorListener implements Listener {
		
		@EventHandler
		public void onLoot(LootGenerateEvent event) {
			
			int total = 0;
			
			int orig = 0;
			
			Player player = (Player) event.getEntity();
			
			for (ItemStack item : event.getLoot()) {
				if (item.getType() == Material.DIAMOND) {
					
					int amt = item.getAmount();
					remaining -= amt;
					
					orig += amt;
					
					int extra = 0;
					
					if (remaining < 0) {
						extra = (int) -remaining;
						remaining = 0;
						
						item.setType(Material.EXPERIENCE_BOTTLE);
						ItemMeta im = item.getItemMeta();
						if (player == null) {
							im.setDisplayName(Locale.get(Language.ZH_CN, "item.experience_bottle.fromdiamond"));
						} else {
							im.setDisplayName(Locale.get(player, "item.experience_bottle.fromdiamond"));
						}
						item.setItemMeta(im);
						item.setAmount(extra);
						
						total += extra;
					}
					
					
				}
			}
			
/////////
	        
	        JSONObject data = JSON.loadDataFromDataBase("dia_logger.json");
	        JSONArray contents = (JSONArray) data.get("logs");
	        
	        JSONObject info = new JSONObject();
	        info.put("type", "loot_table");
	        info.put("who", player.getDisplayName());
	        info.put("amount", orig);
	        
	        contents.add(info);
	        data.put("logs", contents);
	        JSON.saveDataFromDataBase("dia_logger.json", data);
	        
	        /////////
			
			if (total != 0 && event.getEntity().getType() == EntityType.PLAYER) {
				player.sendMessage(String.format(Locale.get(player, "economy.diamonds.loot"), total));
			}
		}
		
	}
	
	private static class DiamondCounterListeners implements Listener {
		
		List<String> ores = StringUtils.fromArgs(Material.DIAMOND_ORE.toString(), Material.DEEPSLATE_DIAMOND_ORE.toString(), Material.LEGACY_DIAMOND_ORE.toString());
		
		
		
		@EventHandler(priority=EventPriority.HIGH)
	    public void onBlockBreak(BlockBreakEvent event){
	        if (event.isCancelled()) return; // skip if the event has been cancelled.
	        
	        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
	        
	        if (ores.contains(event.getBlock().getType().toString())) {
	        	
	        	
	        	if (event.getPlayer().getItemInHand().getType() == Material.IRON_PICKAXE || event.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE || event.getPlayer().getItemInHand().getType() == Material.NETHERITE_PICKAXE) {
	        		replaceDrop(event);
	        	}
	        	
	        	
	        }
	        
	        
	    }
	 
	    public void replaceDrop(BlockBreakEvent event) {
	    	
	    	if (remaining <= 0) {
	    		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "economy.diamonds.attempt.fail"));
	    		event.getPlayer().sendMessage(Locale.get(event.getPlayer(), "economy.diamonds.attempt.info"));
		    	ExperienceOrb orb = (ExperienceOrb) event.getBlock().getLocation().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.EXPERIENCE_ORB);
		    	orb.setExperience(8 + new Random().nextInt(3));
	    		event.setCancelled(true);  // need to cancel to avoid the default drop
		        event.getBlock().setType(Material.AIR);
		        
		        
		        
	    		return;
	    	}
	    	
	    	
	    	
	    	int drops = getDropCount(Material.DIAMOND, getFortuneLevel(event.getPlayer()), new Random(), hasSilkTouch(event.getPlayer()));
	    	
	    	/////////
	        
	        JSONObject data = JSON.loadDataFromDataBase("dia_logger.json");
	        JSONArray contents = (JSONArray) data.get("logs");
	        
	        JSONObject info = new JSONObject();
	        info.put("type", "block_break");
	        info.put("who", event.getPlayer().getDisplayName());
	        info.put("amount", drops);
	        
	        contents.add(info);
	        data.put("logs", contents);
	        JSON.saveDataFromDataBase("dia_logger.json", data);
	        
	        /////////
	    	
	    	
	    	
	    	if (drops > remaining) {
	    		drops = (int) remaining;
	    	}
	    	
	    	ItemStack i = new ItemStack(Material.DIAMOND, drops);
	    	ItemMeta im = i.getItemMeta();
	    	im.setLore(StringUtils.fromArgs("§8§o谁家钻石是蓝色的？","§7价值: §b0.9 ct"));
	    	i.setItemMeta(im);
	    	
	    	
	    	event.getBlock().getLocation().getWorld().dropItemNaturally(event.getBlock().getLocation(), i);
	    	ExperienceOrb orb = (ExperienceOrb) event.getBlock().getLocation().getWorld().spawnEntity(event.getBlock().getLocation().add(0.5, 0.5, 0.5), EntityType.EXPERIENCE_ORB);
	    	orb.setExperience(8 + new Random().nextInt(3));
	    	
	    	
	        event.setCancelled(true);  // need to cancel to avoid the default drop
	        event.getBlock().setType(Material.AIR); // need to replace block since the event has been cancelled.
	        
	        remaining -= drops;
	        
	        if (remaining == 0) {
	        	for (Player player : Bukkit.getOnlinePlayers()) {
	        		StringUtils.systemMessage(player, Locale.get(player, "economy.diamonds"), Locale.get(player, "economy.diamonds.allgone"));
	        	}
	        }
	    }
	    
	    public int getFortuneLevel(Player player) {
	    	return player.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
	    }
	    
	    public boolean hasSilkTouch(Player player) {
	    	return player.getItemInHand().getEnchantmentLevel(Enchantment.SILK_TOUCH) != 0;
	    }
	    
	    public int a(Material material, Random random)
	    {
	        return material == Material.LAPIS_ORE ? 4 + random.nextInt(5) : 1;
	    }

	    public int getDropCount(Material mat, int fortuneLevel, Random random, boolean silktouch)
	    {
	        if ((fortuneLevel > 0) && (!silktouch))
	        {
	            int drops = random.nextInt(fortuneLevel + 2) - 1;
	            if (drops < 0)
	            {
	                drops = 0;
	            }
	            return a(mat, random) * (drops + 1);
	        }
	        return a(mat, random);
	    }
		
	}
	
	public static long remaining = 0;
	public static long bigg = 5000;
	
	public static void load() {
		JSONObject stuff = JSON.loadDataFromDataBase("smp_diamonds.json");
		remaining = (long) stuff.get("number");
	}
	
	public static void save() {
		JSONObject stuff = new JSONObject();
		stuff.put("number", remaining);
		
		JSON.saveDataFromDataBase("smp_diamonds.json", stuff);
	}
	
	public static void beginSavingProcess(Plugin plugin) {
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

			@Override
			public void run() {
				save();
			}
			
		}, 0L, (long) (20 * 60 * 10));
		
	}
	
	public static void enable(Plugin plugin) {
		load();
		Bukkit.getPluginManager().registerEvents(new DiamondCounterListeners(), plugin);
		Bukkit.getPluginManager().registerEvents(new LootGeneratorListener(), plugin);
		beginSavingProcess(plugin);
		
		EventRegistery.register(new DiamondPickUpWatchdog());
	}

}
