package net.itsrelizc.items;

import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

@RelizcNativeMinecraftItem(material=Material.ENCHANTED_BOOK)
public class RelizcOverridenEnchantedBook extends RelizcItemStack {

	public RelizcOverridenEnchantedBook(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<String> renderInternalLore() {
		
		List<String> empty = StringUtils.fromNewList();
		
		EnchantmentStorageMeta meta = (EnchantmentStorageMeta) this.getBukkitItem().getItemMeta();
        if (meta != null) {
            Map<Enchantment, Integer> storedEnchants = meta.getStoredEnchants();
            for (Map.Entry<Enchantment, Integer> entry : storedEnchants.entrySet()) {
                Enchantment enchant = entry.getKey();
                int level = entry.getValue();
                String add = "§b" + Locale.getMojang(owner, "enchantment." + enchant.getKey().getNamespace() + "." + enchant.getKey().getKey());
                add += " " + StringUtils.intToRoman(level);
                empty.add(add);
            }
        }
        
        return empty;
	}

}
