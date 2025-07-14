package net.itsrelizc.health2.fletching;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;

@RelizcNativeMinecraftItem(material=Material.BOW)
@RelizcItemMeta(key="force",type=NBTTagType.TAG_Long,long_init=1306)
@RelizcItemMeta(key="accuracy",type=NBTTagType.TAG_Double,double_init=1.25)
public class RelizcOverridenBow extends RelizcNeoBow {

	public RelizcOverridenBow(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}

}
