package net.itsrelizc.diamonds;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import com.comphenix.packetwrapper.WrapperPlayServerEntityMetadata;
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
import net.itsrelizc.string.ChatUtils;

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
	
	public static void createFor(Player player, long value) {
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

}
