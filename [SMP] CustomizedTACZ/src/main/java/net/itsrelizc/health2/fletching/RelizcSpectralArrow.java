package net.itsrelizc.health2.fletching;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.health2.fletching.ArrowUtils.ArrowFletching;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowPoint;
import net.itsrelizc.health2.fletching.ArrowUtils.ArrowShaft;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.itemlib.RelizcNativeMinecraftItem;
import net.itsrelizc.items.RelizcItemMeta;
import net.itsrelizc.nbt.NBT.NBTTagType;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.LocaleSession;
import net.itsrelizc.string.StringUtils;
	
@RelizcNativeMinecraftItem(material=Material.SPECTRAL_ARROW)
@RelizcItemMeta(key = "point", type = NBTTagType.TAG_Int, int_init=527040694)
@RelizcItemMeta(key = "shaft", type = NBTTagType.TAG_Int, int_init=2032762523)
@RelizcItemMeta(key = "fletching", type = NBTTagType.TAG_Int, int_init=467439461)
public class RelizcSpectralArrow extends RelizcNeoArrow {

	public RelizcSpectralArrow(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}

}
