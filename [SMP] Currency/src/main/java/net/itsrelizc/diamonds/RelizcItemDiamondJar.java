package net.itsrelizc.diamonds;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.itsrelizc.commands.RelizcCommand;
import net.itsrelizc.itemlib.Quality;
import net.itsrelizc.itemlib.RelizcItem;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.players.locales.Locale;

@RelizcItem(id="DIAMOND_JAR", material=Material.POTION, stackable=false, quality=Quality.UNCOMMON, category="INGREDIENTS", placeable=false)
public class RelizcItemDiamondJar extends RelizcItemStack {

	public RelizcItemDiamondJar(Player owner, ItemStack it) {
		super(owner, it);
		// TODO Auto-generated constructor stub
	}
	
	

}
