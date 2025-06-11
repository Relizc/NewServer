package net.itsrelizc.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.itemlib.Quality;
import net.itsrelizc.itemlib.RelizcItem;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.string.StringUtils;

@RelizcItem(id="TEST_SWORD", material=Material.STONE_SWORD, stackable=false, quality=Quality.CONTRABAND, tradeable=false)
@RelizcItemMeta(key="LIQUID", type=NBTTagType.TAG_Int, init=50)
public class RelizcTestSword extends RelizcItemStack {

	public RelizcTestSword(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<String> renderInternalLore() {
		int l = getTagInteger("LIQUID");
		Integer cum = l / 5;
		int remain = 20 - cum;
		String a = "§f" + "|".repeat(cum);
		String b = "§8" + "|".repeat(remain);
		
		
		List<String> lore= StringUtils.fromArgs(
				StringUtils.wrapTextColor(Locale.get(getOwner(), "item.TEST_SWORD.liquid_bar"), getRenderedLanguage().getStandardItemWrapLength()).split("\n"));
		lore.add("  " + Locale.get(getOwner(), "item.TEST_SWORD.liquid_bar_display").formatted(a+b, l));
		return lore;
	}
	
	public void addCum() {
		this.setTagInteger("LIQUID", this.getTagInteger("LIQUID") + 1);
		this.renderDisplay();
		
	}

	
	
}
