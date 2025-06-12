package net.itsrelizc.itemlib;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.*;

import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

/**
 * General item utility
 */
public class ItemUtils {
	
	private static Map<String, Class<? extends RelizcItemStack>> handlers = new HashMap<String, Class<? extends RelizcItemStack>>();
	
	/**
	 * Used only during runtime for plugins to register item classes.
	 * Register by passing in a class that extends {@link RelizcItemStack}<br>
	 * {@code register(RelizcTestSword.class)}
	 * @param <T>
	 * @param itemClass
	 * 
	 * @see net.itsrelizc.itemlib.RelizcItemStack
	 *
	 */
	public static <T extends RelizcItemStack> void register(Class<T> itemClass) {
		RelizcItem annotation = itemClass.getAnnotation(RelizcItem.class);
		String id = null;
		if (annotation == null) {
			RelizcNativeMinecraftItem anno = itemClass.getAnnotation(RelizcNativeMinecraftItem.class); // atempte to get native minecraft annotation.
			id = "MINECRAFT_" + anno.material().toString();
		} else {
			id = annotation.id();
		}
		
		handlers.put(id, itemClass);
		
	}
	
	/**
	 * Gets the class handler associated with this custom / vanilla item.
	 * @param namespace The custom item ID. If the item is a vanilla item, it starts with MINECRAFT_ and plus the Bukkit Material name.
	 * @return The handler class, or null if it does not exist or is not registered using {@link #register(Class)}
	 */
	public static Class<? extends RelizcItemStack> getHandler(String namespace) {
		return handlers.getOrDefault(namespace, null);
	}
	
	/**
	 * Renders the name, quality, and item status (cannot trade, cannot sell, ...). <strong>Does NOT render item statistics</strong>.
	 * For that please use {@link RelizcItem.renderStats}
	 * @param annotation
	 * @param item
	 * @param player
	 * 
	 * @see #renderStats(RelizcItem, ItemStack, Player)
	 */
	protected static void renderNames(RelizcItem annotation, ItemStack item, Player player) {
		
		ItemMeta meta = item.getItemMeta();
		String status = "";
		
		Quality quality;
		
		if (annotation == null) {
			quality = Quality.COMMON;
		} else {
			if (!annotation.tradeable()) {
				status += "§6🚷 " + Locale.get(player, "itemmeta.untradable");
			}
			quality = annotation.quality();
		}
		
		List<String> lore = StringUtils.fromArgs(Locale.get(player, "itemmeta.general.quality", quality.getColor() + Locale.get(player, "itemmeta.quality." + quality.toString())));
		if (status.length() != 0) {
			lore.add(status);
		}
		
		lore.add(" ");
		meta.setLore(lore);
		
		item.setItemMeta(meta);
	}
	
	protected static void renderStats(RelizcItem annotation, ItemStack item, Player player) {
		
	}
	
	/**
	 * Casts a regular minecraft item to a RelizcItemStack, and create necessary NBT tags
	 * if that item does not contain those tags, such as language, item id, and UUID for
	 * unstackable items. The NBT generation process should ONLY happen on regular minecraft
	 * items.
	 * @param player
	 * @param item
	 * @return
	 */
	public static RelizcItemStack castOrCreateItem(Player player, ItemStack item) {
		
		net.minecraft.world.item.ItemStack it = CraftItemStack.asNMSCopy(item);
		
		System.out.println(it.getOrCreateTag().getString("id"));
		if (it.getOrCreateTag().getString("id").length() == 0) {
			CompoundTag tag = it.getOrCreateTag();
			tag.putString("id", "MINECRAFT_" + item.getType().toString());
			
			tag.putString("lang", Profile.findByOwner(player).lang.toString());
			
			if (item.getType().getMaxStackSize() == 1) {
				tag.putUUID("uid", UUID.randomUUID());
			}
			
			Class<? extends RelizcItemStack> handler = getHandler("MINECRAFT_" + item.getType().toString());
			
			it.setTag(tag);
			ItemStack copy = CraftItemStack.asBukkitCopy(it);
			
			ItemMeta meta = copy.getItemMeta();
			
			meta.addItemFlags(ItemFlag.values());
			meta.setDisplayName(Quality.valueOf(it.getRarity()).getColor() + Locale.getMojang(player, copy.getTranslationKey()));
			copy.setItemMeta(meta);
			
			RelizcItemStack completed = null;

			
			if (handler == null) {
				renderNames(null, copy, player);
				
				completed = new RelizcItemStack(player, copy);
				ItemMeta meta2 = completed.getBukkitItem().getItemMeta();
				List<String> l = meta2.getLore();
				l.remove(l.size() - 1);
				meta2.setLore(l);
				completed.getBukkitItem().setItemMeta(meta2);
				
			} else {
				RelizcItem annotation = handler.getAnnotation(RelizcItem.class);
				
				renderNames(annotation, copy, player);
				
				try {
					completed = handler.getDeclaredConstructor(Player.class, ItemStack.class).newInstance(player, copy);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				ItemMeta meta2 = completed.getBukkitItem().getItemMeta();	
				
				List<String> l = meta2.getLore();
				List<String> rendered = completed.renderInternalLore();
				rendered.forEach(s -> l.add(s));
				
				
				
				if (rendered.size() == 0) {
					l.remove(l.size() - 1);
				}
				
				meta2.setLore(l);
				completed.getBukkitItem().setItemMeta(meta2);
			}
			
			
			
			return completed;
		} else {
			return new RelizcItemStack(player, item);
		}
		
	}
	
	/**
	 * Creates an item based on the handler class.
	 * @param <T> A class that extends RelizcItemStack and is annotated with RelizcItem
	 * @param itemClass The handler class that extends RelizcItemStack
	 * @param player The player to create this class for. Mainly for language purpose
	 * @return A RelizcItemStack instance representing that item.
	 */
	public static <T extends RelizcItemStack> T createItem(Class<T> itemClass, Player player) {
		RelizcItem annotation = itemClass.getAnnotation(RelizcItem.class);
		RelizcItemMeta[] metas = itemClass.getAnnotationsByType(RelizcItemMeta.class);
		
		Quality q = annotation.quality();
		String id = annotation.id();
		
		ItemStack created = new ItemStack(annotation.material());
		
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(created);

		CompoundTag tag = nms.getOrCreateTag();
		
		UUID unique = UUID.randomUUID();
		tag.putUUID("uid", unique);
		tag.putString("id", annotation.id());
		tag.putString("lang", Profile.findByOwner(player).lang.toString());
		
		for (RelizcItemMeta meta : metas) {
			NBT.set(tag, meta.key(), meta.type(), meta.init());
		}
		
		
		nms.setTag(tag);
		nms.setHoverName(Component.literal("§r" + q.getColor() + Locale.get(player, "item." + id + ".name")));
		
		ItemStack copy = CraftItemStack.asBukkitCopy(nms);
		ItemMeta meta = copy.getItemMeta();
		
		meta.addItemFlags(ItemFlag.values());
		copy.setItemMeta(meta);
		
		renderNames(annotation, copy, player);
		
		T stack = null;
		try {
			stack = itemClass.getDeclaredConstructor(Player.class, ItemStack.class).newInstance(player, copy);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ItemMeta meta2 = stack.getBukkitItem().getItemMeta();
		List<String> l = meta2.getLore();
		List<String> rendered = stack.renderInternalLore();
		rendered.forEach(s -> l.add(s));
		
		if (rendered.size() == 0) {
			l.remove(l.size() - 1);
		}
		meta2.setLore(l);
		stack.getBukkitItem().setItemMeta(meta2);
		
		return stack;
		
		
		
	}
	

}
