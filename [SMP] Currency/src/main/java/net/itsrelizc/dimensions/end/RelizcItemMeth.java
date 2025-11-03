package net.itsrelizc.dimensions.end;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import net.itsrelizc.itemlib.Quality;
import net.itsrelizc.itemlib.RelizcItem;
import net.itsrelizc.itemlib.RelizcItemStack;
import net.itsrelizc.menus.Skull;
import net.itsrelizc.players.locales.Locale;
import net.itsrelizc.players.locales.Locale.Language;
import net.itsrelizc.string.StringUtils;

@RelizcItem(id="METH", material=Material.BLUE_ICE, stackable=true, quality=Quality.RARE, category="INGREDIENTS", placeable=false)
public class RelizcItemMeth extends RelizcItemStack {
	
	public RelizcItemMeth(Player owner, ItemStack it) {
		super(owner, it);
//		
//		SkullMeta meta = (SkullMeta) it.getItemMeta();
//		
//		Skull.setSkullUrl(meta, "http://textures.minecraft.net/texture/ddba642efffa13ec3730eafc5914ab68115c1f998803f74452e2e0cd26af0b8");
//		it.setItemMeta(meta);
	}
	
	@Override
	public List<String> renderInternalLore() {
		
		String[] prod = StringUtils.wrapWithColor(Locale.a(owner, "item.meth.description"), Language.ZH_CN).split("\n");
		return StringUtils.fromArgs(prod);
		
	}

}
 