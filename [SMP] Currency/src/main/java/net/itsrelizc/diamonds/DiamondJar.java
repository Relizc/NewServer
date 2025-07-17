package net.itsrelizc.diamonds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;
import org.bukkit.potion.Potion;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.reflect.accessors.FieldAccessor;
import com.comphenix.protocol.utility.StreamSerializer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.nbt.NbtBase;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.itemlib.ItemUtils;
import net.itsrelizc.itemlib.ItemUtils.MetadataPair;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

public class DiamondJar implements Listener {
	
	public static class DiamondJarPacketListener {
		
		private static Entity getEntityById(int entityId) {
	        for (Entity entity : Bukkit.getWorlds().stream().flatMap(world -> world.getEntities().stream()).toList()) {
	            if (entity.getEntityId() == entityId) {
	                return entity;
	            }
	        }
	        return null;
	    }
		
		@Deprecated
		public static void enable() {
			ProtocolManager manager = ProtocolLibrary.getProtocolManager();
			manager.addPacketListener(new PacketAdapter(EventRegistery.main, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA) {
				
			    @Override
			    public void onPacketSending(PacketEvent event) {
			    	
//			    	ChatUtils.broadcastSystemMessage("entitymetadata", "metadata");
			        
			    	Player targetPlayer = event.getPlayer();
	                PacketContainer packet = event.getPacket();

	                // Extract the entity ID from the packet
	                int entityId = packet.getIntegers().read(0);

	                // Fetch the entity using the entity ID
	                Entity entity = getEntityById(entityId);
	                if (entity == null) {
	                    return;
	                }

	                // Modify the metadata
//	                List<WrappedWatchableObject> metadata = packet.getWatchableCollectionModifier().read(0);
//	                WrappedDataWatcher dataWatcher = new WrappedDataWatcher(metadata);

	                // Change the custom name (index 2 usually corresponds to custom name in Spigot)
	                WrappedDataWatcher.WrappedDataWatcherObject nameKey =
	                        new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.getChatComponentSerializer(true));
//	                dataWatcher.setObject(nameKey, "{\"text\":\"Custom Name for " + targetPlayer.getName() + "\"}");

	                // Write back the modified metadata
//	                packet.getWatchableCollectionModifier().write(0, dataWatcher.getWatchableObjects());
			    	
			    }
			    
			});
		}
		
	}
	
	public static ItemStack createFor(Player player, long value) {
		
		RelizcItemDiamondJar pot = ItemUtils.createItem(RelizcItemDiamondJar.class, player, new MetadataPair("VALUE", value));
		
		player.getInventory().addItem(pot.getBukkitItem());
		
		return pot.getBukkitItem();
	}
	
	@EventHandler
	public void a(PlayerDropItemEvent event) {
		
		RelizcItemStack stack = ItemUtils.castOrCreateItem(event.getItemDrop().getItemStack());
		
		if (stack.getID().equals("DIAMOND_JAR")) {
//			ChatUtils.broadcastSystemMessage("diamondtest", "dropped bottle");
			
			long val = stack.getTagLong("VALUE");
//			ChatUtils.broadcastSystemMessage("diamondtest", String.valueOf(im.getCustomTagContainer().getCustomTag(key, ItemTagType.LONG)));

			event.getItemDrop().setCustomNameVisible(true);
			event.getItemDrop().setCustomName("§b%,d ct".formatted(val));
		} else if (event.getItemDrop().getItemStack().getType() == Material.DIAMOND) {
			event.getItemDrop().setCustomNameVisible(true);
			event.getItemDrop().setCustomName("§b%,d ct".formatted(event.getItemDrop().getItemStack().getAmount() * 100));
		}
	}
	
	@EventHandler
    public void onItemMerge(ItemMergeEvent event) {
        Item source = event.getEntity(); // the item that will merge
        Item target = event.getTarget(); // the item being merged into
        
        if (!(source.getItemStack().getType() == Material.DIAMOND)) return;

        target.setCustomName("§b%,d ct".formatted((target.getItemStack().getAmount() + source.getItemStack().getAmount())  * 100));
        
        // Optionally cancel the merge
        // event.setCancelled(true);
    }
	
	@EventHandler
	public void brew(InventoryClickEvent event) {
		
		if (event.getInventory().getType() == InventoryType.BREWING) {
			if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.POTION) {
				
				RelizcItemStack stack = ItemUtils.castOrCreateItem(event.getCurrentItem());
				Long value = stack.getTagLong("RELIZC:VALUE");
				
				if (value == null) return;
				
				event.setCancelled(true);
				
				Player player = (Player) event.getWhoClicked();
				
				player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1f, 1f);
				event.getWhoClicked().sendMessage(Locale.get((Player) event.getWhoClicked(), "item.diamond_potion.dontbrew"));
				
			}
		}
		
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent e) {

        EntityType entity = e.getEntity().getType();
        
       
        
//        //(cause.toString() + " " + e.getEntity().isDead() + " " +  e.getEntity().getLastDamageCause());
        
        if ( e.getEntity().getLastDamageCause() != null) return;

        if (entity == EntityType.DROPPED_ITEM) {
        	
        	
        	
        	Item it = (Item) e.getEntity();
        	ItemStack item = it.getItemStack();
        	
        	
        	if (item.getType() == Material.DIAMOND) {
//        		it.getLocation().getWorld().spawnParticle(Particle.PORTAL, it.getLocation(), 100, 0, 0, 0, 0.5f);
//				it.getLocation().getWorld().spawnParticle(Particle.GLOW, it.getLocation(), 20, 0, 0, 0, 0.5f);
//				it.getLocation().getWorld().playSound(it.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 0f);
//				
//        		DiamondCounter.remaining += item.getAmount();
        		e.setCancelled(true);
        		
        		it.setInvulnerable(true);
        		
        		return;
        	}
        	
        	if (item.getType() != Material.POTION) return;
        	RelizcItemStack ris = ItemUtils.castOrCreateItem(item);
        	Long value = ris.getTagLong("VALUE");
			
			if (value == null) return;
			
			if (value % 100 != 0) {

				ris.setMetadata("VALUE", value % 100);
				
				it.setItemStack(ris.getBukkitItem());
				it.setCustomName("§b%,d ct".formatted(value % 100));
				it.setInvulnerable(true);;
				
				
				e.setCancelled(true);
				
			} else {
				it.remove();
			}
			
			long amt = value / 100;
			
			it.getLocation().getWorld().spawnParticle(Particle.PORTAL, it.getLocation(), 100, 0, 0, 0, 0.5f);
			it.getLocation().getWorld().spawnParticle(Particle.GLOW, it.getLocation(), 20, 0, 0, 0, 0.5f);
			it.getLocation().getWorld().playSound(it.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 0f);
			
			
			while (amt > 0) {
				long thisstack = Math.min(amt, 64);
				
				RelizcItemStack dias = ItemUtils.castOrCreateItem(ItemGenerator.generate(Material.DIAMOND, (int) thisstack));
				
				Item ent = it.getLocation().getWorld().dropItemNaturally(it.getLocation(), dias.getBukkitItem());
				ent.setCustomName("§b%,d ct".formatted(100 * thisstack));
				ent.setInvulnerable(true);
				amt -= thisstack;
			}
        	
        }
    }

}
