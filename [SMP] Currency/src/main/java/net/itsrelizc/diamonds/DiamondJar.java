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
		ItemStack pot = ItemGenerator.generate(Material.POTION, 1, Locale.get(player, "item.diamond_jar.name"), Locale.get(player, "item.diamond_jar.lore"), Locale.get(player, "item.diamond_jar.lore_2").formatted(value / 1000f));
		PotionMeta meta = (PotionMeta) pot.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		meta.setColor(Color.AQUA);
		
		
		NamespacedKey key = new NamespacedKey(EventRegistery.main, "diamondValue");
		meta.getCustomTagContainer().setCustomTag(key, ItemTagType.LONG, value);
		
		pot.setItemMeta(meta);
		player.getInventory().addItem(pot);
		
		return pot;
	}
	
	@EventHandler
	public void a(PlayerDropItemEvent event) {
		if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§b钻石瓶")) {
//			ChatUtils.broadcastSystemMessage("diamondtest", "dropped bottle");
			
			ItemMeta im = event.getItemDrop().getItemStack().getItemMeta();
			NamespacedKey key = new NamespacedKey(EventRegistery.main, "diamondValue");
//			ChatUtils.broadcastSystemMessage("diamondtest", String.valueOf(im.getCustomTagContainer().getCustomTag(key, ItemTagType.LONG)));

			event.getItemDrop().setCustomNameVisible(true);
			event.getItemDrop().setCustomName("§b%,.3f ct".formatted(im.getCustomTagContainer().getCustomTag(key, ItemTagType.LONG) / 1000.0));
		} else if (event.getItemDrop().getItemStack().getType() == Material.DIAMOND) {
			event.getItemDrop().setCustomNameVisible(true);
			event.getItemDrop().setCustomName("§b0.100 ct");
		}
	}
	
	@EventHandler
	public void brew(InventoryClickEvent event) {
		
		if (event.getInventory().getType() == InventoryType.BREWING) {
			if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.POTION) {
				
				Long value = event.getCurrentItem().getItemMeta().getCustomTagContainer().getCustomTag(new NamespacedKey(EventRegistery.main, "diamondValue"), ItemTagType.LONG);
				
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

        EntityDamageEvent.DamageCause cause = e.getCause();
        double damage = e.getDamage();
        double finalDamage = e.getFinalDamage();
        EntityType entity = e.getEntity().getType();
        
       
        
//        Bukkit.broadcastMessage(cause.toString() + " " + e.getEntity().isDead() + " " +  e.getEntity().getLastDamageCause());
        
        if ( e.getEntity().getLastDamageCause() != null) return;

        if (entity == EntityType.DROPPED_ITEM) {
        	
        	
        	
        	Item it = (Item) e.getEntity();
        	ItemStack item = it.getItemStack();
        	
        	if (item.getType() == Material.DIAMOND) {
        		it.getLocation().getWorld().spawnParticle(Particle.PORTAL, it.getLocation(), 100, 0, 0, 0, 0.5f);
				it.getLocation().getWorld().spawnParticle(Particle.GLOW, it.getLocation(), 20, 0, 0, 0, 0.5f);
				it.getLocation().getWorld().playSound(it.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 0f);
				
        		DiamondCounter.remaining += item.getAmount();
        		return;
        	}
        	
        	if (item.getType() != Material.POTION) return;
        	
        	Long value = item.getItemMeta().getCustomTagContainer().getCustomTag(new NamespacedKey(EventRegistery.main, "diamondValue"), ItemTagType.LONG);
			
			if (value == null) return;
			
			if (value % 1000 != 0) {
				it.getLocation().getWorld().spawnParticle(Particle.PORTAL, it.getLocation(), 100, 0, 0, 0, 0.5f);
				it.getLocation().getWorld().spawnParticle(Particle.GLOW, it.getLocation(), 20, 0, 0, 0, 0.5f);
				it.getLocation().getWorld().playSound(it.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 0f);
				
				
				
			}
			
			long amt = value / 100;
			
			Item ent = it.getLocation().getWorld().dropItemNaturally(it.getLocation(), ItemGenerator.generate(Material.DIAMOND, (int) amt));
			ent.setInvulnerable(true);
        	
        }
    }

}
