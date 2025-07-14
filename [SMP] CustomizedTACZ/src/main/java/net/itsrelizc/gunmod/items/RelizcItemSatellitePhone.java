package net.itsrelizc.gunmod.items;

import java.util.Arrays;
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

@RelizcItem(id="SATELLITE_PHONE", material=Material.PLAYER_HEAD, stackable=false, quality=Quality.EPIC, category="TOOLS", placeable=false)
public class RelizcItemSatellitePhone extends RelizcItemStack {
	
	public RelizcItemSatellitePhone(Player owner, ItemStack it) {
		super(owner, it);
		
		SkullMeta meta = (SkullMeta) it.getItemMeta();
		
		Skull.setSkullUrl(meta, "http://textures.minecraft.net/texture/938b05e50ef1c8c02ce30d8d09b846e7e9145ac4117157f6530b4d8e1e329854");
		it.setItemMeta(meta);
	}
	
	@Override
	public List<String> renderInternalLore() {
		
		String[] prod = StringUtils.wrapWithColor(Locale.a(owner, "item.SATELLITE_PHONE.description"), Language.ZH_CN).split("\n");
		return StringUtils.fromArgs(prod);
		
	}

}
 