package net.itsrelizc.estate.chestshop;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.google.gson.JsonParser;

import de.blablubbabc.insigns.SignSendEvent;
import net.itsrelizc.commands.CommandRegistery;
import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.events.TaskDelay;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.smp.currency.SafeTransactions;
import net.itsrelizc.string.StringUtils;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.WallSign;
import org.bukkit.block.sign.Side;
import org.bukkit.block.sign.SignSide;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ChestShop implements Listener {
    private ProtocolManager protocolManager;
    private final NamespacedKey shopDataKey = new NamespacedKey(EventRegistery.main, "shop_data");
    Plugin insignsPlugin;

    public void enable() {
        EventRegistery.register(this);
        protocolManager = ProtocolLibrary.getProtocolManager();
//        registerPacketListener();
        

//        }
        
        
        CommandRegistery.register(new ChestShopCommand());
    }

    private void registerPacketListener() {
//    	
    }
    
   

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        String firstLine = event.getLine(0);
        
        if (firstLine == null) return;
        
        boolean isBuy = firstLine.equalsIgnoreCase("[shop]");
        
        if (isBuy) {
        	
//        	event.setCancelled(true);
        	
            
            try {
                String[] item1Data = event.getLine(1).split(" ");
                String[] item2Data = event.getLine(2).split(" ");
                
                
                
                int item1Amount = Integer.parseInt(item1Data[0]);
                Material item1Type = Material.matchMaterial(item1Data[1].replace(":", "_"), true);
                int item2Amount = Integer.parseInt(item2Data[0]);
                Material item2Type = Material.matchMaterial(item2Data[1].replace(":", "_"), true);
                
                if (item1Type == null || item2Type == null) {
                    player.sendMessage(Locale.get(player, "chestshop.error.invalid_item"));
                    player.playSound(player, Sound.ENTITY_VILLAGER_NO, 2f, 1f);
                    return;
                }
                
                
                
                Block signBlock = event.getBlock();
                Sign sign = (Sign) signBlock.getState();
                PersistentDataContainer data = sign.getPersistentDataContainer();
                data.set(shopDataKey, PersistentDataType.STRING, firstLine + ":" + item1Amount + ":" + item1Type.name() + ":" + item2Amount + ":" + item2Type.name());
                sign.update();
                
                player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 2f, 2f);
                player.sendMessage(Locale.get(player, "chestshop.message.shop_created", (isBuy ? Locale.get(player, "chestshop.label.buying") : Locale.get(player, "chestshop.label.selling"))));
                
                String item1Name = Locale.getMojang(player, item1Type.getTranslationKey());
                String item2Name = Locale.getMojang(player, item2Type.getTranslationKey());
                
                String[] finallines = {"", "", "", ""};
                
                
                
                TaskDelay.delayTask(new Runnable() {

					@Override
					public void run() {
//						for (int i = 0; i < 4; i ++) {
////		                	event.setLine(i, finallines[i]);
//		                	//sign.setLine(i, finallines[i]);
//		                	sign.getSide(Side.FRONT).setLine(1, "LOSER");
//		                	
//		                	
//		                }
						
						for (Player p : Bukkit.getOnlinePlayers()) {
							
							finallines[0] = "§a§l" + Locale.get(p, "chestshop.trading");
			                finallines[1] = "§r" + item1Amount + "× §b" + item1Name;
			                finallines[2] = "§a§l" + Locale.get(p, "chestshop.label.for");
			                finallines[3] = "§r" + item2Amount + "× §b" + item2Name;
							
		                	p.sendSignChange(event.getBlock().getLocation(), finallines);
		                }
					}
                	
                }, 0L);
                
//                
                
                
            } catch (Exception e) {
            	player.playSound(player, Sound.ENTITY_VILLAGER_YES, 2f, 2f);
                player.sendMessage(Locale.get(player, "chestshop.error.invalid_format"));
                
                TextComponent more = new TextComponent(Locale.get(player, "chestshop.more_info").split("%s")[0]);
                TextComponent more2 = new TextComponent(Locale.get(player, "chestshop.more_info").split("%s")[1]);
                
                TextComponent link = new TextComponent("§b§n/chestshop");
                StringUtils.attachCommand(link, "chestshop", null);
                more.addExtra(link);
                more.addExtra(more2);
                
                player.spigot().sendMessage(more);
            }
        }
    }
    
    public static Block findAttachedChest(Block signBlock) {
        // Ensure the block is a sign
        if (!(signBlock.getState() instanceof Sign)) {
            return null;
        }

        BlockData blockData = signBlock.getBlockData();
        
        if (blockData instanceof WallSign) {
            // Wall sign: Get the block it is attached to
            WallSign wallSign = (WallSign) blockData;
            return signBlock.getRelative(wallSign.getFacing().getOppositeFace());
        } else if (blockData instanceof org.bukkit.block.data.type.Sign) {
            // Standing sign: Get the block below it
            return signBlock.getRelative(0, -1, 0);
        }
        
        return null; // Should never reach here if signBlock is a valid sign
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    	
    	if (event.getHand() == EquipmentSlot.OFF_HAND) return;
    	
    	if (event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST && event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType().toString().endsWith("SIGN")) {
          TextComponent more = new TextComponent(Locale.get(event.getPlayer(), "chestshop.more_info").split("%s")[0]);
          TextComponent more2 = new TextComponent(Locale.get(event.getPlayer(), "chestshop.more_info").split("%s")[1]);
          
          TextComponent link = new TextComponent("§b§n/chestshop");
          StringUtils.attachCommand(event.getPlayer(), link, "chestshop");
          more.addExtra(link);
          more.addExtra(more2);
          
          event.getPlayer().spigot().sendMessage(more);
          event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_TRADE, 0.5f, 2f);
    	}
    	
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null || !(block.getState() instanceof Sign || block.getState() instanceof WallSign)) return;

        Sign sign = (Sign) block.getState();
        Player player = event.getPlayer();
        PersistentDataContainer data = sign.getPersistentDataContainer();
        String shopData = data.get(shopDataKey, PersistentDataType.STRING);
        
//        Bukkit.broadcastMessage("bruh");
        
        if (shopData == null) return;
        
        event.setCancelled(true);
        
        String[] parts = shopData.split(":");
        boolean isBuy = parts[0].equalsIgnoreCase("[shop]");
        int item1Amount = Integer.parseInt(parts[1]);
        Material item1Type = Material.matchMaterial(parts[2]);
        int item2Amount = Integer.parseInt(parts[3]);
        Material item2Type = Material.matchMaterial(parts[4]);
        
//        Bukkit.broadcastMessage(item1Type + " " + item2Type);
       
        if (item1Type == null || item2Type == null) return;
        
        Block chestBlock = findAttachedChest(block);
        if (!(chestBlock.getState() instanceof Chest)) {
        	player.playSound(player, Sound.ENTITY_VILLAGER_NO, 2f, 1f);
            player.sendMessage(Locale.get(player, "chestshop.error.no_chest"));
            
//            TextComponent more = new TextComponent(Locale.get(player, "chestshop.more_info").split("%s")[0]);
//            TextComponent more2 = new TextComponent(Locale.get(player, "chestshop.more_info").split("%s")[0]);
//            
//            TextComponent link = new TextComponent("§b/chestshop");
//            StringUtils.attachCommand(link, "chestshop", null);
//            more.addExtra(link);
//            more.addExtra(more2);
//            
//            player.spigot().sendMessage(more);
            
            return;
            
            
        }
        
        
        
        

        Chest chest = (Chest) chestBlock.getState();
        Inventory chestInventory = chest.getInventory();
        
        if (isBuy) {
        	
        	//Bukkit.broadcastMessage(item1Type + " " + item2Type);
        	
        	if (!(countEmptySlots(chestInventory) * item1Type.getMaxStackSize() >= item1Amount)) {
        		player.playSound(player, Sound.ENTITY_VILLAGER_NO, 2f, 1f);
                player.sendMessage(Locale.get(player, "chestshop.error.insufficient_sell_items.chest"));
        		return;
        	}
        	if (!(countEmptySlots(player.getInventory()) * item2Type.getMaxStackSize() >= item2Amount)) {
        		player.playSound(player, Sound.ENTITY_VILLAGER_NO, 2f, 1f);
                player.sendMessage(Locale.get(player, "chestshop.error.insufficient_sell_items.player"));
        		return;
        	}
        	if (!containsAtLeast(chestInventory, new ItemStack(item2Type), item2Amount)) {
        		player.playSound(player, Sound.ENTITY_VILLAGER_NO, 2f, 1f);
                player.sendMessage(Locale.get(player, "chestshop.error.insufficient_buy_items.chest"));
        		return;
        	} else if (!containsAtLeast(player.getInventory(), new ItemStack(item1Type), item1Amount)) {
        		player.playSound(player, Sound.ENTITY_VILLAGER_NO, 2f, 1f);
                player.sendMessage(Locale.get(player, "chestshop.error.insufficient_buy_items.player"));
        		return;
        	}
        	
            if (containsAtLeast(chestInventory, new ItemStack(item2Type), item2Amount) &&
                containsAtLeast(player.getInventory(), new ItemStack(item1Type), item1Amount)) {
                
                try {
					SafeTransactions.trade(chestInventory, player.getInventory(), new ItemStack(item2Type, item2Amount));
					SafeTransactions.trade(player.getInventory(), chestInventory, new ItemStack(item1Type, item1Amount));
					player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 2f, 2f);
	                player.sendMessage(Locale.get(player, "chestshop.message.purchase_success"));
				} catch (Exception e) {
					player.playSound(player, Sound.BLOCK_NOTE_BLOCK_BASS, 2f, 0f);
					player.sendMessage(Locale.get(player, "chestshop.message.purchase_error"));
				}

                
            } 
        } 
    }
    
    public static boolean containsAtLeast(Inventory inv, ItemStack it, int amount) {
    	int c = 0;
    	for (ItemStack item : inv) {
    		if (item == null) continue;
    		if (item.getType() == it.getType()) {
    			c += item.getAmount();
    		}
    		
    		if (c >= amount) return true;
    	}
    	
    	return false;
    }
    
    @EventHandler
    public void mov(PlayerMoveEvent event) {
    	
    	int fromChunkX = event.getFrom().getBlockX() >> 4;
        int fromChunkZ = event.getFrom().getBlockZ() >> 4;
        int toChunkX = event.getTo().getBlockX() >> 4;
        int toChunkZ = event.getTo().getBlockZ() >> 4;

        // Check if the chunk actually changed
        if (fromChunkX != toChunkX || fromChunkZ != toChunkZ) {
        	
        	for (int dx = -2; dx <= 2; dx ++) {
        		for (int dz = -2; dz <= 2; dz ++) {
        			
        			Chunk chunk = event.getPlayer().getWorld().getChunkAt(dx + event.getTo().getChunk().getX(), dz + event.getTo().getChunk().getZ());
        			
        			//Bukkit.broadcastMessage(chunk.toString());
        			
        			for (BlockState state : chunk.getTileEntities()) {
            			if (state instanceof Sign) {
            				
            				Sign sign = (Sign) state;
            				
            				PersistentDataContainer data = sign.getPersistentDataContainer();
            		        String shopData = data.get(shopDataKey, PersistentDataType.STRING);
            		        
//            		        Bukkit.broadcastMessage("bruh");
            		        
            		        if (shopData == null) return;
            		        
            		        String[] parts = shopData.split(":");
            		        boolean isBuy = parts[0].equalsIgnoreCase("[shop]");
            		        int item1Amount = Integer.parseInt(parts[1]);
            		        Material item1Type = Material.matchMaterial(parts[2]);
            		        int item2Amount = Integer.parseInt(parts[3]);
            		        Material item2Type = Material.matchMaterial(parts[4]);
            		        
            		        String[] finallines = {"", "", "", ""};
            		        
            		        Player p = event.getPlayer();
            		        
            		        String item1Name = Locale.getMojang(p, item1Type.getTranslationKey());
			                String item2Name = Locale.getMojang(p, item2Type.getTranslationKey());
							
							finallines[0] = "§a§l" + Locale.get(p, "chestshop.trading");
			                finallines[1] = "§r" + item1Amount + "× §b" + item1Name;
			                finallines[2] = "§a§l" + Locale.get(p, "chestshop.label.for");
			                finallines[3] = "§r" + item2Amount + "× §b" + item2Name;
							
		                	event.getPlayer().sendSignChange(sign.getLocation(), finallines);

            			}
            		}
        		}
        	}
        	
        }

    	
    	
    	
    }
    
    public static int countEmptySlots(Inventory inventory) {
        int emptySlots = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR) {
                emptySlots++;
            }
        }
        return emptySlots;
    }
}
