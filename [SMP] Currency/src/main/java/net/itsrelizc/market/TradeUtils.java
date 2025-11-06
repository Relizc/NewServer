package net.itsrelizc.market;

import org.bukkit.entity.Player;

import net.itsrelizc.players.locales.Locale;

public class TradeUtils {
	
	public static final String SHOP_DASH = "\n§8§m---------------";
	
	public static String SHOP_DASH_OF(Player player) {
		return SHOP_DASH + "\n§7" + Locale.a(player, "shop.gonnaspend") + "§8:§r";
	}

}
