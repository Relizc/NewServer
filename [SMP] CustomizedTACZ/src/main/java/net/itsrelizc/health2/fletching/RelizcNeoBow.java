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


public class RelizcNeoBow extends RelizcItemStack {

	public RelizcNeoBow(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}
	
	private static String getColor(double resistance) {
		
		String build = "";
		if (resistance > 0) {
			build += "§c-%.1f%%";
		} else if (resistance < 0) {
			build += "§a+%.1f%%";
		} else {
			build += "§70%";
			return build;
		}
		
		return build.formatted(Math.abs(resistance) * 100);
	}
	
	private static String getAccuracy(double resistance) {
		String build = "";
		if (resistance > 0) {
			build += "§a+%.2f";
		} else if (resistance < 0) {
			build += "§c-%.2f";
		} else {
			build += "§70";
			return build;
		}
		
		return build.formatted(Math.abs(resistance));
	}
	
	@Override
	public List<String> renderInternalLore() {
	    // TODO: Add dynamic lore rendering logic
		
		LocaleSession loc = new LocaleSession(owner);
		
		long force = this.getTagLong("force");
		double accuracy = this.getTagDouble("accuracy");
		
		
	    return StringUtils.fromArgs(loc.a("item.bow." + this.getID().toLowerCase() + ".description"), " ", 
	    		loc.a("item.bow.force").formatted(force),
	    		loc.a("item.arrow.accuracy").formatted(accuracy));
	}

	
}
