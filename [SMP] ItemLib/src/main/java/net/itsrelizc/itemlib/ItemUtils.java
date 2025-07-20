package net.itsrelizc.itemlib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CreativeCategory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.Profile;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;

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
	
	public static Set<String> getRegisteredIDs() {
		return handlers.keySet();
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
	 * Renders the name, quality, enchantment, and item status (cannot trade, cannot sell, ...). <strong>Does NOT render item statistics</strong>.
	 * For that please use {@link RelizcItem.renderStats}. No new item is created during this process
	 * @param annotation
	 * @param item
	 * @param player
	 * @param lang 
	 * 
	 * @see #renderStats(RelizcItem, ItemStack, Player)
	 */
	protected static void renderNames(RelizcItem annotation, ItemStack item, Player player, Language lang) {
		
		ItemMeta meta = item.getItemMeta();
		String status = "";
		
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		
		Quality quality;
		
		if (annotation == null) {
			quality = Quality.fromNMSRarity(nmsItem.getRarity());
		} else {
			if (!annotation.tradeable()) {
				status += "§6🚷 " + Locale.get(player, "itemmeta.untradable");
			}
			quality = annotation.quality();
		}
		
		String cat;
		if (annotation == null || annotation.category().equals("DEFAULT")) {
			cat = "";
		} else {
			cat = "itemmeta.category." + annotation.category();
		}
		
		List<String> lore = StringUtils.fromArgs(Locale.get(player, "itemmeta.general.quality").formatted(quality.getColor() + Locale.get(player, "itemmeta.quality." + quality.toString())) + " §8" + Locale.a(player, cat));
	
		if (status.length() != 0) {
			lore.add(status);
		}
		
		lore.add(" ");
		
		////("asshole " + item.getEnchantments().size());
		
		for (Entry<Enchantment, Integer> ench : item.getEnchantments().entrySet()) {
			String namespace = "enchantment.";
			namespace += ench.getKey().getKey().getNamespace().toLowerCase() + ".";
			namespace += ench.getKey().getKey().getKey().toLowerCase();
			String levl = StringUtils.intToRoman(ench.getValue());
			
			lore.add("§b" + Locale.getMojang(player, namespace) + " " + levl);
		}
		if (item.getEnchantments().size() > 0) lore.add(" ");
		
		
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
	 * @return The inferred type (which extends RelizcItemStack), or RelizcItemStack object itself if it cannot be cast automatically
	 */
	public static <T extends RelizcItemStack> T castOrCreateItem(Player player, ItemStack item) {
		Language lang = Language.ZH_CN;
		if (player != null) lang = Profile.findByOwner(player).lang;
		return castOrCreateItem(player, item, lang);
	}
	
	
	
	/**
	 * Casts a regular minecraft item to a RelizcItemStack, and create necessary NBT tags
	 * if that item does not contain those tags, such as language, item id, and UUID for
	 * unstackable items. The NBT generation process should ONLY happen on regular minecraft
	 * items.
	 * @param player
	 * @param item
	 * @return The inferred type (which extends RelizcItemStack), or RelizcItemStack object itself if it cannot be cast automatically
	 */
	public static <T extends RelizcItemStack> T castOrCreateItem(Player player, ItemStack item, Language lang) {
		
		net.minecraft.world.item.ItemStack it = CraftItemStack.asNMSCopy(item);
	
		
		//System.out.println(it.getOrCreateTag().getString("id"));
		if (it.getOrCreateTag().getString("id").length() == 0) {
			CompoundTag tag = it.getOrCreateTag();
			tag.putString("id", "MINECRAFT_" + item.getType().toString());
			
			tag.putString("lang", lang.toString());
			
			if (item.getType().getMaxStackSize() == 1) {
				tag.putUUID("uid", UUID.randomUUID());
			}
			
			Class<? extends RelizcItemStack> handler = getHandler("MINECRAFT_" + item.getType().toString());
			
			it.setTag(tag);
			ItemStack copy = CraftItemStack.asBukkitCopy(it);
			
			
			
			ItemMeta meta = copy.getItemMeta();
			
			meta.addItemFlags(ItemFlag.values());
			
			if (tag.getString("CUSTOM_NAME") != null && tag.getString("CUSTOM_NAME").length() > 0) {
				String name = tag.getString("CUSTOM_NAME")
						.replace("&r","")
						.replace("&o", "")
						.replace("&0", "§0")
						.replace("&1", "§0")
						.replace("&2", "§0")
						.replace("&3", "§0")
						.replace("&4", "§0")
						.replace("&5", "§0")
						.replace("&6", "§0")
						.replace("&7", "§0")
						.replace("&8", "§0")
						.replace("&9", "§0")
						.replace("&a", "§0")
						.replace("&b", "§0")
						.replace("&c", "§0")
						.replace("&d", "§0")
						.replace("&e", "§0")
						.replace("&f", "§0")
						.replace("&k", "§0")
						.replace("&l", "§0")
						.replace("&m", "§0")
						.replace("&&", "&");
				meta.setDisplayName(name);
			} else {
				meta.setDisplayName(Quality.valueOf(it.getRarity()).getColor() + Locale.getMojang(lang, copy.getTranslationKey()));
			}
			
			
			copy.setItemMeta(meta);
			
			T completed = null;
			
			

			
			if (handler == null) {
				renderNames(null, copy, player, lang);
				try {
					completed = (T) new RelizcItemStack(player, copy);
				} catch (Exception e) {
					return null;
				}
				
				ItemMeta meta2 = completed.getBukkitItem().getItemMeta();
				List<String> l = meta2.getLore();
				if (tag.getString("CUSTOM_NAME") != null && tag.getString("CUSTOM_NAME").length() > 0) {
					l.add(Quality.valueOf(it.getRarity()).getColor() + Locale.getMojang(lang, copy.getTranslationKey()));
				}
				l.remove(l.size() - 1);
				meta2.setLore(l);
				completed.getBukkitItem().setItemMeta(meta2);
				
			} else {
				RelizcItem annotation = handler.getAnnotation(RelizcItem.class);
				RelizcItemMeta[] metas = handler.getAnnotationsByType(RelizcItemMeta.class);
				net.minecraft.world.item.ItemStack it2 = CraftItemStack.asNMSCopy(copy);
				tag = it2.getOrCreateTag();
				for (RelizcItemMeta m : metas) {
					if (!tag.contains(m.key())) {
						if (m.type() == NBTTagType.TAG_Int) tag.putInt(m.key(), m.int_init());
						else if (m.type() == NBTTagType.TAG_Long) tag.putLong(m.key(), m.long_init());
						else if (m.type() == NBTTagType.TAG_String) tag.putString(m.key(), m.str_init());
						else if (m.type() == NBTTagType.TAG_Double) tag.putDouble(m.key(), m.double_init());
					}
				}
				it2.setTag(tag);
				copy = CraftItemStack.asBukkitCopy(it2);
				
				
				renderNames(annotation, copy, player, lang);
				
				try {
					completed = (T) handler.getDeclaredConstructor(Player.class, ItemStack.class).newInstance(player, copy);
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					return null;
				}
				
				ItemMeta meta2 = completed.getBukkitItem().getItemMeta();	
				
				List<String> l = meta2.getLore();
				List<String> rendered = completed.renderInternalLore();
				if (rendered.size() > 0) {
					if (item.getEnchantments().size() > 0) {
						l.add(" ");
					}
				}
				rendered.forEach(s -> l.add(s));
				
				
				
				if (rendered.size() == 0) {
					l.remove(l.size() - 1);
				} 
				
				meta2.setLore(l);
				completed.getBukkitItem().setItemMeta(meta2);
			}
			
			
			
			
			return completed;
		} else {
			
			String code = it.getOrCreateTag().getString("id");
			Class<? extends RelizcItemStack> handle = ItemUtils.getHandler(code);
			
			if (handle == null) {
				try {
					return (T) new RelizcItemStack(player, item);
				} catch (Exception e) {
					return null;
				}
			} else {
				try {
					return (T) handle.getDeclaredConstructor(Player.class, ItemStack.class).newInstance(player, item);
				} catch (Exception e) {
					return null;
				}
			}
			
		}
		
	}
	
	public static class MetadataPair {
		
		public Object value;
		public String key;

		public MetadataPair(String key, Object value) {
			this.key = key;
			this.value = value;
		}
		
	}
	
	/**
	 * Creates an item based on the handler class.
	 * @param <T> A class that extends RelizcItemStack and is annotated with RelizcItem
	 * @param itemClass The handler class that extends RelizcItemStack
	 * @param player The player to create this class for. Mainly for language purpose
	 * @return A RelizcItemStack instance representing that item.
	 */
	public static <T extends RelizcItemStack> T createItem(Class<T> itemClass, Player player, MetadataPair... defaultMetadatas) {
		RelizcItem annotation = itemClass.getAnnotation(RelizcItem.class);
		Quality q;
		String id;
		Material mat;
		if (annotation == null) {
			RelizcNativeMinecraftItem item = itemClass.getAnnotation(RelizcNativeMinecraftItem.class);
			q = Quality.fromNMSRarity(CraftItemStack.asNMSCopy(new ItemStack(item.material())).getRarity());
			id = "MINECRAFT_" + item.material().toString();
			mat = item.material();
		} else {
			q = annotation.quality();
			id = annotation.id();
			mat = annotation.material();
		}
		
		RelizcItemMeta[] metas = itemClass.getAnnotationsByType(RelizcItemMeta.class);
		
		Method method;
		ItemStack created = null;
		try {
			method = itemClass.getDeclaredMethod("getGeneratedItemStack");
			created = (ItemStack) method.invoke(null, player);  // Output: Hello, World!
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			created = null;
			//e.printStackTrace();
		}

        // Step 3: Call the method with null as the object, since it's static
        
		
		

			
		if (created == null) created = new ItemStack(mat);

		
		
		net.minecraft.world.item.ItemStack nms = CraftItemStack.asNMSCopy(created);

		CompoundTag tag = nms.getOrCreateTag();
		
		UUID unique = UUID.randomUUID();
		if (mat.getMaxStackSize() == 1) {
			tag.putUUID("uid", unique);
		}
		tag.putString("id", id);
		
		
		String lang;
		if (Profile.findByOwner(player) == null) {
			lang = Language.ZH_CN.toString();
		} else {
			lang = Profile.findByOwner(player).lang.toString();
		}
		
		tag.putString("lang", lang);
		
		for (RelizcItemMeta meta : metas) {
			boolean found = false;
//			NBT.set(tag, meta.key(), meta.type(), meta.init());
			for (MetadataPair def : defaultMetadatas) {
				if (meta.key().equals(def.key)) {
					if (meta.type() == NBTTagType.TAG_Int) {
						tag.putInt(meta.key(), (int) def.value);
					} else if (meta.type() == NBTTagType.TAG_String) {
						tag.putString(meta.key(), (String) def.value);
					} else if (meta.type() == NBTTagType.TAG_Long) {
						tag.putLong(meta.key(), (long) def.value);
					} else if (meta.type() == NBTTagType.TAG_Double) {
						tag.putDouble(meta.key(), (double) def.value);
					}
					found = true;
					break;
				}
			}
			
			if (!found) {
				if (meta.type() == NBTTagType.TAG_Int) {
					tag.putInt(meta.key(), meta.int_init());
				} else if (meta.type() == NBTTagType.TAG_String) {
					tag.putString(meta.key(), meta.str_init());
				} else if (meta.type() == NBTTagType.TAG_Long) {
					tag.putLong(meta.key(), meta.long_init());
				} else if (meta.type() == NBTTagType.TAG_Double) {
					tag.putDouble(meta.key(), meta.double_init());
				}
			}
			
		}
		
		
		nms.setTag(tag);
		//nms.setHoverName(Component.literal("§r" + q.getColor() + Locale.get(player, "item." + id + ".name")));
		
		ItemStack copy = CraftItemStack.asBukkitCopy(nms);
		ItemMeta meta = copy.getItemMeta();
		
		meta.addItemFlags(ItemFlag.values());
		copy.setItemMeta(meta);
		
		renderNames(annotation, copy, player, Language.valueOf(lang));
		
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
		
		if (tag.getString("CUSTOM_NAME") != null && tag.getString("CUSTOM_NAME").length() > 0) {
			String name = tag.getString("CUSTOM_NAME")
					.replace("&r","")
					.replace("&o", "")
					.replace("&0", "§0")
					.replace("&1", "§0")
					.replace("&2", "§0")
					.replace("&3", "§0")
					.replace("&4", "§0")
					.replace("&5", "§0")
					.replace("&6", "§0")
					.replace("&7", "§0")
					.replace("&8", "§0")
					.replace("&9", "§0")
					.replace("&a", "§0")
					.replace("&b", "§0")
					.replace("&c", "§0")
					.replace("&d", "§0")
					.replace("&e", "§0")
					.replace("&f", "§0")
					.replace("&k", "§0")
					.replace("&l", "§0")
					.replace("&m", "§0")
					.replace("&&", "&");
			meta2.setDisplayName(name);
			l.add(q.getColor() + stack.renderName());
		} else {
			meta2.setDisplayName(q.getColor() + stack.renderName());
		}
		
		List<String> rendered = stack.renderInternalLore();
		rendered.forEach(s -> l.add(s));
		
		if (rendered.size() == 0) {
			l.remove(l.size() - 1);
		}
		meta2.setLore(l);
		stack.getBukkitItem().setItemMeta(meta2);
		
		return stack;
		
		
		
	}
	
	//public static 
	
	/**
	 * Casts a regular minecraft item to a RelizcItemStack, and create necessary NBT tags
	 * if that item does not contain those tags, such as language, item id, and UUID for
	 * unstackable items. The NBT generation process should ONLY happen on regular minecraft
	 * items.
	 * @param player
	 * @param item
	 * @return The inferred type (which extends RelizcItemStack), or RelizcItemStack object itself if it cannot be cast automatically
	 */
	public static <T extends RelizcItemStack> T castOrCreateItem(ItemStack content) {
		return castOrCreateItem(null, content);
	}
	
	
	public static Class<? extends RelizcItemStack> getHandler(RelizcItemStack it) {
		return getHandler(it.getID());
	}
	
	
	public static RelizcItem getItemInfo(RelizcItemStack it) {
		Class<? extends RelizcItemStack> clazz = getHandler(it);
		if (clazz == null) return null;
		
		return getHandler(it).getAnnotation(RelizcItem.class);
	}
	
	
	


}
