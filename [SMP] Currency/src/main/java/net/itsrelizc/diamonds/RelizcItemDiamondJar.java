package net.itsrelizc.diamonds;

import java.util.List;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import net.itsrelizc.itemlib.Quality;
import net.itsrelizc.itemlib.RelizcItem;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.menus.ItemGenerator;
import net.itsrelizc.nbt.NBT;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;
import net.minecraft.nbt.CompoundTag;

@RelizcItem(id="DIAMOND_JAR", material=Material.POTION, stackable=false, quality=Quality.UNCOMMON, category="INGREDIENTS", placeable=false)
@RelizcItemMeta(key="VALUE", long_init=0, type = NBTTagType.TAG_Long)
public class RelizcItemDiamondJar extends RelizcItemStack {

	public RelizcItemDiamondJar(Player owner, ItemStack it) {
		super(owner, it);
		
		long disp = NBT.getLong(NBT.getNBT(it), "VALUE");

		PotionMeta meta = (PotionMeta) it.getItemMeta();
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		meta.addEnchant(Enchantment.ARROW_DAMAGE, 1, false);
		meta.setColor(Color.AQUA);
		it.setItemMeta(meta);
		
		
		
		
	}
	
	@Override
	public String renderName() {
		return Locale.a(owner, "item.DIAMOND_JAR.name");
		
	}
	
	@Override
	public List<String> renderInternalLore() {
		return StringUtils.fromArgs(Locale.a(null, "item.diamond_jar.lore"), " ", Locale.a(null, "item.diamond_jar.lore_2").formatted(this.getTagLong("VALUE")));
	}

}
