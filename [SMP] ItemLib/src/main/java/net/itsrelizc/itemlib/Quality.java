package net.itsrelizc.itemlib;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.world.item.Rarity;

public enum Quality {
	
	COMMON(ChatColor.WHITE),
	UNCOMMON(ChatColor.YELLOW),
	RARE(ChatColor.AQUA),
	EPIC(ChatColor.LIGHT_PURPLE),
	LEGEND(ChatColor.GREEN),
	RELIC(ChatColor.GOLD),
	HOLY(ChatColor.RED),
	CONTRABAND(ChatColor.DARK_AQUA);
	
	private ChatColor color;

	Quality(ChatColor gray) {
		color = gray;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
	/**
	 * Translates NMS's Rarity class to Quality
	 * @param rarity
	 * @return
	 */
	static Quality valueOf(Rarity rarity) {
		if (rarity == Rarity.COMMON) return COMMON;
		else if (rarity == Rarity.UNCOMMON) return UNCOMMON;
		else if (rarity == Rarity.EPIC) return EPIC;
		else if (rarity == Rarity.RARE) return RARE;
		return COMMON;
	}
	
}
