package net.itsrelizc.diamonds;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.string.ChatUtils;

public class DiamondUtils {
	
	public static double scanValueThenCreate(ItemStack i) {
		
		ItemMeta im = i.getItemMeta();
		
		if (im.getLore() != null) {
			
			boolean f = false;
			for (String s : im.getLore()) {
				if (s.startsWith("§7价值: §b")) {
					return Double.valueOf(s.substring("§7价值: §b".length(), s.length() - 4));
					
				}
			}
			
			
		} else {
			im.setLore(ChatUtils.fromArgs("§8§o谁家钻石是蓝色的？"));
		}
		
		List<String> lore = im.getLore();
		lore.add("§7价值: §b0.9 ct");
		im.setLore(lore);
		
		i.setItemMeta(im);
		
		return -1;
		
		
		
	}

}