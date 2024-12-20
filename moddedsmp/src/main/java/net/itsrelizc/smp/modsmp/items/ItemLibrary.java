package net.itsrelizc.smp.modsmp.items;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.string.ChatUtils;

public class ItemLibrary {
	
	public static ItemStack item_itemduper() {
		
		ItemStack i = new ItemStack(Material.SCULK_SHRIEKER);
		
		i.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
		
		ItemMeta im = i.getItemMeta();
		im.addItemFlags(ItemFlag.values());
		im.setDisplayName("§e物品复制器");
		
		im.setLore(ChatUtils.fromArgs("§7贾斯丁有限公司™ 研发出的 FTX-6090 精密物品复制器",
				"",
				"§8贾斯丁™科技唯一序列号：" + new Random().nextInt(2147483647),
				"§8严禁盗版使用！"));
		
		i.setItemMeta(im);
		
		return i;
		
	}
	
	public static ItemStack item_itemduper_shopedition() {
		
		ItemStack i = new ItemStack(Material.SCULK_SHRIEKER);
		
		i.addUnsafeEnchantment(Enchantment.ARROW_DAMAGE, 1);
		
		ItemMeta im = i.getItemMeta();
		im.addItemFlags(ItemFlag.values());
		im.setDisplayName("§7×1 §e物品复制器");
		
		im.setLore(ChatUtils.fromArgs("§7贾斯丁有限公司™ 研发出的 FTX-6090 精密物品复制器","","§7§m------------", "§7物品价格: §f×10 §b钻石"));
		
		i.setItemMeta(im);
		
		return i;
		
	}
	
	public static ItemStack item_instakill() {
		
		ItemStack i = ItemGenerator.generate(Material.GOLDEN_AXE, 0, null);
		return i;
		
	}
	
}
