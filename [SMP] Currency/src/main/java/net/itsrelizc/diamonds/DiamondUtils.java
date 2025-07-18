package net.itsrelizc.diamonds;

import java.util.List;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.tags.ItemTagType;

import net.itsrelizc.events.EventRegistery;
import net.itsrelizc.string.StringUtils;

public class DiamondUtils {
	
	public static Long scanValueThenCreate(ItemStack i) {
		
		ItemMeta im = i.getItemMeta();
		
		if (im.getLore() != null) {
			
			boolean f = false;
			for (String s : im.getLore()) {
				if (s.startsWith("§7价值: §b")) {
					return Long.valueOf(s.substring("§7价值: §b".length(), s.length() - 4));
					
				}
			}
			
			
		} else {
			im.setLore(StringUtils.fromArgs("§8§o谁家钻石是蓝色的？"));
		}
		
		List<String> lore = im.getLore();
		lore.add("§7价值: §b1 ct");
		im.setLore(lore);
		
		NamespacedKey key = new NamespacedKey(EventRegistery.main, "diamondValue");
		im.getCustomTagContainer().setCustomTag(key, ItemTagType.LONG, 1l);
		
		i.setItemMeta(im);
		
		return -1l;
		
		
		
	}

}
