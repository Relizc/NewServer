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

@RelizcNativeMinecraftItem(material=Material.ARROW)
@RelizcItemMeta(key = "point", type = NBTTagType.TAG_Int, int_init=-992184946)
@RelizcItemMeta(key = "shaft", type = NBTTagType.TAG_Int, int_init=-1478126976)
@RelizcItemMeta(key = "fletching", type = NBTTagType.TAG_Int, int_init=-488142409)
public class RelizcNeoArrow extends RelizcItemStack {

	public RelizcNeoArrow(Player owner, ItemStack it) {
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
			build += "§c+%.2f";
		} else if (resistance < 0) {
			build += "§a-%.2f";
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
		ArrowPoint point = ArrowUtils.getPoint(this.getTagInteger("point"));
		ArrowShaft shaft = ArrowUtils.getShaft(this.getTagInteger("shaft"));
		ArrowFletching fletching = ArrowUtils.getFletching(this.getTagInteger("fletching"));
		
	    return StringUtils.fromArgs(loc.a("item.arrow.description"), " ", 
	    		loc.a("item.arrow.mass").formatted(point.seedWeight + shaft.seedWeight + fletching.seedWeight),
	    		loc.a("item.arrow.damage").formatted(point.damage),
	    		loc.a("item.arrow.penetration").formatted(ArrowUtils.convertPenetrationToSymbol((int) point.penetration)),
	    		loc.a("item.arrow.breaking").formatted(ArrowUtils.breakChanceToColor(shaft.releaseBreak), shaft.releaseBreak * 100d, ArrowUtils.breakChanceToColor(shaft.flightBreak), shaft.flightBreak * 100),
	    		loc.a("item.arrow.speed").formatted(getColor(fletching.resistance)),
	    		loc.a("item.arrow.accuracy").formatted(getAccuracy(fletching.stability)),
	    		" ",
	    		loc.a("item.arrow.point").formatted(loc.a("item.arrow_point.name." + point.id)),
	    		loc.a("item.arrow.shaft").formatted(loc.a("item.arrow_shaft.name." + shaft.id)),
	    		loc.a("item.arrow.fletching").formatted(loc.a("item.arrow_fletching.name." + fletching.id)));
	}
}
