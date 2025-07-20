package net.itsrelizc.health2.fletching;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.gunmod.items.armor.RelizcDamageable;
import net.itsrelizc.players.locales.LocaleSession;
import net.itsrelizc.string.StringUtils;


public class RelizcNeoBow extends RelizcDamageable {

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
	public String[] getCopyMetadata() {
		return new String[]{"LONG;force", "DOUBLE;accuracy"};
	}
	
	
	
	@Override
	public List<String> renderInternalLore() {
	    // TODO: Add dynamic lore rendering logic
		
		LocaleSession loc = new LocaleSession(owner);
		
		long force = this.getTagLong("force");
		double accuracy = this.getTagDouble("accuracy");
		
		
	    return StringUtils.fromArgs(loc.a("item.bow." + this.getID().toLowerCase() + ".description"), " ", renderDurability(), " ",
	    		loc.a("item.bow.force").formatted(force),
	    		loc.a("item.arrow.accuracy").formatted(accuracy));
	}

	
}
