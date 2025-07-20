package net.itsrelizc.gunmod.items.armor;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;


// CLASS: double 
// RICOCHET: double percentage
// DURABILITY: int
// MAX_DURABILITY: int
@RelizcNativeMinecraftItem(material = Material.LEATHER_HELMET)
@RelizcItemMeta(key="RICOCHET",type=NBTTagType.TAG_Double, double_init=0.05)
@RelizcItemMeta(key="DURABILITY",type=NBTTagType.TAG_Int, int_init=0) // ignore
@RelizcItemMeta(key="MAX_DURABILITY",type=NBTTagType.TAG_Int, int_init=-1) // Set to -1 to convert from vanilla item
public class RelizcOverridenLeatherHelmet extends RelizcNeoArmor {
	
	@Override
	protected int getNewDurability() {return 55;}
	
	public RelizcOverridenLeatherHelmet(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}
	

}
 