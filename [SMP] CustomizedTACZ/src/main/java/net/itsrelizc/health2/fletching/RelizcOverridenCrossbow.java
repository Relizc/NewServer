package net.itsrelizc.health2.fletching;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;

@RelizcNativeMinecraftItem(material=Material.CROSSBOW)
@RelizcItemMeta(key="force",type=NBTTagType.TAG_Long,long_init=2612)
@RelizcItemMeta(key="accuracy",type=NBTTagType.TAG_Double,double_init=1.15)
public class RelizcOverridenCrossbow extends RelizcNeoCrossbow {

	public RelizcOverridenCrossbow(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}

}
