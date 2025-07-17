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
import net.itsrelizc.string.StringUtils;

@RelizcItem(id="TEST_SWORD", material=Material.STONE_SWORD, stackable=false, quality=Quality.CONTRABAND, tradeable=false)
@RelizcItemMeta(key="LIQUID", type=NBTTagType.TAG_Int, int_init=50)
public class RelizcOverridedWoodenSword extends RelizcItemStack{

	public RelizcOverridedWoodenSword(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}

}
